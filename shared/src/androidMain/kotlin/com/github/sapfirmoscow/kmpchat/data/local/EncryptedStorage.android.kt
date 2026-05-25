package com.github.sapfirmoscow.kmpchat.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class AndroidEncryptedStorage(
    context: Context
) : EncryptedStorage {

    private val sp = context.getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE)

    private fun getOrCreateKey(): SecretKey {
        val keystore = KeyStore.getInstance("AndroidKeyStore").also { it.load(null) }
        keystore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            .also { it.init(spec) }
            .generateKey()
    }

    override fun saveString(key: String, value: String) {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        sp.edit { putString(key, Base64.encodeToString(iv + encrypted, Base64.DEFAULT)) }
    }

    override fun deleteString(key: String) {
        sp.edit { remove(key) }
    }

    override fun getString(key: String): String? {
        val raw = sp.getString(key, null) ?: return null
        return runCatching {
            val combined = Base64.decode(raw, Base64.DEFAULT)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_BITS, combined, 0, GCM_IV_BYTES)
            )
            String(cipher.doFinal(combined, GCM_IV_BYTES, combined.size - GCM_IV_BYTES), Charsets.UTF_8)
        }.getOrNull()
    }

    private companion object {
        const val SHARED_PREF_FILE_NAME = "chat_private_data"
        const val KEY_ALIAS = "kmpchat_token_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
    }
}
