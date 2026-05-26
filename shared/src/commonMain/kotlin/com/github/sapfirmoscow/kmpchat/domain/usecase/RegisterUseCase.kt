package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.AuthApi
import com.github.sapfirmoscow.kmpchat.domain.model.User
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject

/**
 * Use-case для регистрации нового пользователя.
 *
 * Выполняет запрос к [AuthApi], сохраняет JWT-токен в [JWTTokenRepository]
 * и данные пользователя в [CurrentUserRepository].
 *
 * @param authApi               API-клиент аутентификации.
 * @param jwtTokenRepository    Репозиторий для хранения токена.
 * @param currentUserRepository Репозиторий для хранения данных текущего пользователя.
 */
internal class RegisterUseCase @Inject constructor(
    private val authApi: AuthApi,
    private val jwtTokenRepository: JWTTokenRepository,
    private val currentUserRepository: CurrentUserRepository
) {
    /**
     * Выполняет регистрацию, сохраняет токен и данные пользователя.
     *
     * @param username Желаемое имя пользователя.
     * @param password Пароль.
     */
    suspend operator fun invoke(username: String, password: String) {
        val response = authApi.register(username, password)
        jwtTokenRepository.saveToken(response.token)
        currentUserRepository.saveCurrentUser(
            User(
                id = response.user.id,
                username = response.user.username,
                createdAt = response.user.createdAt
            )
        )
    }
}