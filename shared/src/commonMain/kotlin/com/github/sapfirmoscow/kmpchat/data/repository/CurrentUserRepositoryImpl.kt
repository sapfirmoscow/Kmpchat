package com.github.sapfirmoscow.kmpchat.data.repository

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.domain.model.User
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository

/**
 * Реализация [CurrentUserRepository], хранящая данные пользователя
 * в [EncryptedStorage] по отдельным ключам.
 *
 * @param encryptedStorage Зашифрованное хранилище для персистентности данных.
 */
internal class CurrentUserRepositoryImpl(
    private val encryptedStorage: EncryptedStorage
) : CurrentUserRepository {

    override fun getCurrentUser(): User? {
        val id = encryptedStorage.getString(KEY_ID) ?: return null
        val username = encryptedStorage.getString(KEY_USERNAME) ?: return null
        val createdAt = encryptedStorage.getString(KEY_CREATED_AT) ?: return null
        return User(id = id, username = username, createdAt = createdAt)
    }

    override fun saveCurrentUser(user: User) {
        encryptedStorage.saveString(KEY_ID, user.id)
        encryptedStorage.saveString(KEY_USERNAME, user.username)
        encryptedStorage.saveString(KEY_CREATED_AT, user.createdAt)
    }

    override fun clearCurrentUser() {
        encryptedStorage.deleteString(KEY_ID)
        encryptedStorage.deleteString(KEY_USERNAME)
        encryptedStorage.deleteString(KEY_CREATED_AT)
    }

    private companion object {
        const val KEY_ID = "CURRENT_USER_ID"
        const val KEY_USERNAME = "CURRENT_USER_USERNAME"
        const val KEY_CREATED_AT = "CURRENT_USER_CREATED_AT"
    }
}
