package com.github.sapfirmoscow.kmpchat.data.local

import java.io.File
import java.security.KeyStore
import java.util.Base64
import java.util.Properties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec

 class JvmEncryptedStorage : EncryptedStorage {

    private val storageDir = File(System.getProperty("user.home"), ".kmpchat").also { it.mkdirs() }
    private val keystoreFile = File(storageDir, "keystore.p12")
    private val propsFile = File(storageDir, "secure_prefs.properties")

    private val keystorePassword: CharArray by lazy { deriveKeystorePassword() }

    private fun deriveKeystorePassword(): CharArray {
        val seed = "kmpchat:${System.getProperty("user.name")}:${System.getProperty("user.home")}"
        val spec = PBEKeySpec(seed.toCharArray(), KEYSTORE_SALT, 10_000, 256)
        val bytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(bytes).toCharArray()
    }

    private fun getOrCreateKey(): SecretKey {
        val keystore = KeyStore.getInstance("PKCS12")
        if (keystoreFile.exists()) {
            keystoreFile.inputStream().use { keystore.load(it, keystorePassword) }
            (keystore.getKey(KEY_ALIAS, keystorePassword) as? SecretKey)?.let { return it }
        } else {
            keystore.load(null, keystorePassword)
        }

        val key = KeyGenerator.getInstance("AES").also { it.init(256) }.generateKey()
        keystore.setEntry(KEY_ALIAS, KeyStore.SecretKeyEntry(key), KeyStore.PasswordProtection(keystorePassword))
        keystoreFile.outputStream().use { keystore.store(it, keystorePassword) }
        return key
    }

    override fun saveString(key: String, value: String) {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        val props = loadProps()
        props.setProperty(key, Base64.getEncoder().encodeToString(iv + encrypted))
        propsFile.outputStream().use { props.store(it, null) }
    }

    override fun deleteString(key: String) {
        val props = loadProps()
        props.remove(key)
        propsFile.outputStream().use { props.store(it, null) }
    }

    override fun getString(key: String): String? {
        val encoded = loadProps().getProperty(key) ?: return null
        return runCatching {
            val combined = Base64.getDecoder().decode(encoded)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_BITS, combined, 0, GCM_IV_BYTES)
            )
            String(cipher.doFinal(combined, GCM_IV_BYTES, combined.size - GCM_IV_BYTES), Charsets.UTF_8)
        }.getOrNull()
    }

    private fun loadProps() = Properties().also {
        if (propsFile.exists()) propsFile.inputStream().use(it::load)
    }

    private companion object {
        const val KEY_ALIAS = "kmpchat_token_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
        val KEYSTORE_SALT = "kmpchat_keystore_salt".toByteArray()
    }
}