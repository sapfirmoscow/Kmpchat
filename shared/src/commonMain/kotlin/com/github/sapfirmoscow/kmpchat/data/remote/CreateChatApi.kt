package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.dto.ChatDto
import com.github.sapfirmoscow.kmpchat.data.dto.CreateChatRequest
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Клиент API для создания DM-чата.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor (с Bearer-авторизацией).
 */
internal class CreateChatApi @Inject constructor(
    private val httpClient: HttpClient
) {
    /**
     * Создаёт DM-чат с указанным пользователем.
     * Если чат уже существует, возвращает его без создания дубликата.
     *
     * @param memberId Идентификатор собеседника.
     * @return [ChatDto] — созданный или существующий чат.
     */
    suspend fun createChat(memberId: String): ChatDto {
        return httpClient.post(CHATS_URL) {
            contentType(ContentType.Application.Json)
            setBody(CreateChatRequest(memberId))
        }.body()
    }

    private companion object {
        const val CHATS_URL = "http://192.168.1.234:8080/chats"
    }
}
