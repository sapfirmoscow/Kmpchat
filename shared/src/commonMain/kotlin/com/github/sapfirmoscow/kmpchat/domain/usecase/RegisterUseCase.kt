package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.AuthApi
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject

/**
 * Use-case для регистрации нового пользователя.
 *
 * Выполняет запрос к [AuthApi] и сохраняет полученный JWT-токен
 * в [JWTTokenRepository].
 *
 * @param authApi            API-клиент аутентификации.
 * @param jwtTokenRepository Репозиторий для хранения токена.
 */
internal class RegisterUseCase @Inject constructor(
    private val authApi: AuthApi,
    private val jwtTokenRepository: JWTTokenRepository
) {
    /**
     * Выполняет регистрацию и сохраняет токен.
     *
     * @param username Желаемое имя пользователя.
     * @param password Пароль.
     */
    suspend operator fun invoke(username: String, password: String) {
        val response = authApi.register(username, password)
        jwtTokenRepository.saveToken(response.token)
    }
}