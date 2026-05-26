package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.AuthApi
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject

/**
 * Use-case для входа в систему.
 *
 * Выполняет запрос к [AuthApi] и сохраняет полученный JWT-токен
 * в [JWTTokenRepository].
 *
 * @param authApi            API-клиент аутентификации.
 * @param jwtTokenRepository Репозиторий для хранения токена.
 */
internal class LoginUseCase @Inject constructor(
    private val authApi: AuthApi,
    private val jwtTokenRepository: JWTTokenRepository
) {
    /**
     * Выполняет вход и сохраняет токен.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     */
    suspend operator fun invoke(username: String, password: String) {
        val response = authApi.login(username, password)
        jwtTokenRepository.saveToken(response.token)
    }
}
