package com.github.sapfirmoscow.kmpchat.domain.repository

import com.github.sapfirmoscow.kmpchat.domain.model.User

/**
 * Репозиторий для хранения данных текущего авторизованного пользователя.
 *
 * Позволяет в любом месте приложения получить информацию о себе:
 * например, чтобы отличить своё сообщение / себя в списке участников чата.
 */
interface CurrentUserRepository {

    /**
     * Возвращает данные авторизованного пользователя или `null`,
     * если пользователь не вошёл в систему.
     */
    fun getCurrentUser(): User?

    /**
     * Сохраняет данные пользователя после успешного входа или регистрации.
     *
     * @param user Авторизованный пользователь.
     */
    fun saveCurrentUser(user: User)

    /** Удаляет сохранённые данные пользователя (вызывается при выходе). */
    fun clearCurrentUser()
}
