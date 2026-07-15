package com.github.sapfirmoscow.kmpchat.data.client

import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import com.github.sapfirmoscow.kmpchat.domain.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Создаёт и настраивает экземпляр [HttpClient] для работы с API.
 *
 * Клиент включает:
 * - Bearer-авторизацию с автоматической подгрузкой токена из [jwtTokenRepository];
 * - логирование запросов/ответов;
 * - поддержку JSON с игнорированием неизвестных полей.
 *
 * @param jwtTokenRepository Репозиторий для получения и удаления JWT-токена.
 * @param sessionManager     Получает уведомление, когда сервер отверг токен (401).
 * @return Готовый к использованию [HttpClient].
 */
internal fun createHttpClient(
     jwtTokenRepository: JWTTokenRepository,
     sessionManager: SessionManager
): HttpClient = HttpClient {
    install(WebSockets)
    install(Auth) {
        bearer {
//            sendWithoutRequest { request ->
//                !request.url.encodedPath.endsWith("/register")
//            }
            loadTokens {
                jwtTokenRepository.getToken()?.let { BearerTokens(accessToken = it, refreshToken = null) }
            }
            // Обновлять нечего — сервер не выдаёт refresh-токен, поэтому 401 означает
            // конец сессии. Сообщаем об этом наружу и отдаём null: повторять запрос бессмысленно.
            refreshTokens {
                jwtTokenRepository.deleteToken()
                sessionManager.notifyExpired()
                null
            }
        }
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}