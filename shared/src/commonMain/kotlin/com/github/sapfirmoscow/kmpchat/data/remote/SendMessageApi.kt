package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.dto.MessageDto
import com.github.sapfirmoscow.kmpchat.data.dto.SendMessageRequest
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Клиент API для отправки сообщений в чат.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor (с Bearer-авторизацией).
 */
internal class SendMessageApi @Inject constructor(
    private val httpClient: HttpClient
) {
    /**
     * Отправляет сообщение в указанный чат.
     *
     * @param chatId Идентификатор чата.
     * @param text   Текст сообщения (1–2000 символов).
     * @return [MessageDto] — сохранённое сообщение.
     */
    suspend fun sendMessage(chatId: String, text: String): MessageDto {
        return httpClient.post("$BASE_URL/$chatId/messages") {
            contentType(ContentType.Application.Json)
            setBody(SendMessageRequest(text))
        }.body()
    }

    private companion object {
        const val BASE_URL = "http://192.168.1.234:8080/chats"
    }
}
