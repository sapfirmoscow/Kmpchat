package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.dto.LoginRequest
import com.github.sapfirmoscow.kmpchat.data.dto.LoginResponse
import com.github.sapfirmoscow.kmpchat.data.dto.RegisterRequest
import com.github.sapfirmoscow.kmpchat.data.dto.RegisterResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Клиент API для операций аутентификации.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor.
 * @param apiConfig  Адреса backend-сервера.
 */
internal class AuthApi @Inject constructor(
    private val httpClient: HttpClient,
    private val apiConfig: ApiConfig
) {
    /**
     * Регистрирует нового пользователя.
     *
     * @param username Желаемое имя пользователя.
     * @param password Пароль.
     * @return [RegisterResponse] с JWT-токеном и данными пользователя.
     */
    suspend fun register(username: String, password: String): RegisterResponse {
        return httpClient.post("${apiConfig.baseUrl}/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username, password))
        }.body()
    }

    /**
     * Выполняет вход существующего пользователя.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     * @return [LoginResponse] с JWT-токеном.
     */
    suspend fun login(username: String, password: String): LoginResponse {
        return httpClient.post("${apiConfig.baseUrl}/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }.body()
    }
}