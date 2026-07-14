package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.dto.MessageDto
import com.github.sapfirmoscow.kmpchat.data.dto.WsEventDto
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

/**
 * Клиент WebSocket для получения новых сообщений в реальном времени.
 *
 * Авторизация передаётся через заголовок `Sec-WebSocket-Protocol`,
 * как того требует сервер.
 *
 * @param httpClient           Настроенный HTTP-клиент Ktor (с плагином WebSockets).
 * @param jwtTokenRepository   Репозиторий для получения JWT-токена.
 * @param apiConfig            Адреса backend-сервера.
 */
internal class ChatWebSocketApi @Inject constructor(
    private val httpClient: HttpClient,
    private val jwtTokenRepository: JWTTokenRepository,
    private val apiConfig: ApiConfig
) {
    /**
     * Возвращает [Flow], который эмитит входящие сообщения через WebSocket.
     * Flow завершается при закрытии соединения или отмене коллектором.
     */
    fun observeMessages(): Flow<MessageDto> = flow {
        val token = jwtTokenRepository.getToken() ?: return@flow
        val session = httpClient.webSocketSession(apiConfig.wsUrl) {
            headers.append("Sec-WebSocket-Protocol", "access_token, $token")
        }
        try {
            for (frame in session.incoming) {
                if (frame is Frame.Text) {
                    val event = runCatching { json.decodeFromString<WsEventDto>(frame.readText()) }.getOrNull()
                    if (event?.type == EVENT_NEW_MESSAGE) {
                        emit(event.data)
                    }
                }
            }
        } finally {
            session.close()
        }
    }

    private companion object {
        const val EVENT_NEW_MESSAGE = "message.new"
        val json = Json { ignoreUnknownKeys = true }
    }
}
