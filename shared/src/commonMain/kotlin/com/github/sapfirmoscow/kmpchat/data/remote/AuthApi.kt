package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.client.ApiException
import com.github.sapfirmoscow.kmpchat.data.client.bodyOrThrow
import com.github.sapfirmoscow.kmpchat.data.dto.LoginRequest
import com.github.sapfirmoscow.kmpchat.data.dto.LoginResponse
import com.github.sapfirmoscow.kmpchat.data.dto.RegisterRequest
import com.github.sapfirmoscow.kmpchat.data.dto.RegisterResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

/**
 * Клиент API для операций аутентификации.
 *
 * Запросы помечены [AuthCircuitBreaker]: 401 от `/login` означает неверный пароль,
 * а не истёкшую сессию, поэтому Bearer-плагин не должен пытаться «обновить» токен
 * и сообщать [com.github.sapfirmoscow.kmpchat.domain.session.SessionManager] об истечении.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor.
 * @param apiConfig  Адреса backend-сервера.
 * @throws ApiException из всех методов, если сервер вернул ошибку.
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
            attributes.put(AuthCircuitBreaker, Unit)
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username, password))
        }.bodyOrThrow(::describeRegisterError)
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
            attributes.put(AuthCircuitBreaker, Unit)
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }.bodyOrThrow(::describeLoginError)
    }

    private fun describeRegisterError(e: ApiException): String? = when (e.status) {
        HttpStatusCode.Conflict -> "Это имя пользователя уже занято"
        // Сюда попадаем, только если локальная проверка разошлась с серверной.
        HttpStatusCode.BadRequest -> e.serverMessage?.let { "Сервер отклонил данные: $it" }
        else -> null
    }

    private fun describeLoginError(e: ApiException): String? = when (e.status) {
        HttpStatusCode.Unauthorized -> "Неверное имя пользователя или пароль"
        else -> null
    }
}
