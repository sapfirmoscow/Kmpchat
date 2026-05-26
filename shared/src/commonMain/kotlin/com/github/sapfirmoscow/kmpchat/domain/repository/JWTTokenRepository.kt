package com.github.sapfirmoscow.kmpchat.domain.repository

/** Репозиторий для хранения JWT-токена текущей сессии. */
interface JWTTokenRepository {
    /** Возвращает сохранённый токен или `null`, если пользователь не авторизован. */
    fun getToken(): String?

    /**
     * Сохраняет JWT-токен.
     *
     * @param token Токен, полученный от сервера после входа или регистрации.
     */
    fun saveToken(token: String)

    /** Удаляет токен, фактически завершая сессию пользователя. */
    fun deleteToken()
}