package com.github.sapfirmoscow.kmpchat.data.repository

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository

/**
 * Реализация [JWTTokenRepository], хранящая токен в [EncryptedStorage].
 *
 * @param encryptedStorage Зашифрованное хранилище для персистентности токена.
 */
internal class JWTTokenRepositoryImpl(
    private val encryptedStorage: EncryptedStorage
) : JWTTokenRepository {

    override fun getToken(): String? =
        encryptedStorage.getString(key = TOKEN_KEY)


    override fun saveToken(token: String) {
        encryptedStorage.saveString(key = TOKEN_KEY, value = token)
    }

    override fun deleteToken() {
        encryptedStorage.deleteString(key = TOKEN_KEY)
    }

    private companion object {
        const val TOKEN_KEY = "JWT_TOKEN"
    }
}