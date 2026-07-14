package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.dto.MessagesResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Клиент API для загрузки истории сообщений чата.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor (с Bearer-авторизацией).
 */
internal class GetMessagesApi @Inject constructor(
    private val httpClient: HttpClient
) {
    /**
     * Загружает историю сообщений из указанного чата.
     *
     * @param chatId Идентификатор чата.
     * @param limit  Максимальное количество сообщений (по умолчанию 50, макс. 100).
     * @return [MessagesResponse] со списком сообщений.
     */
    suspend fun getMessages(chatId: String, limit: Int = 50): MessagesResponse {
        return httpClient.get("$BASE_URL/$chatId/messages") {
            parameter("limit", limit)
        }.body()
    }

    private companion object {
        const val BASE_URL = "http://192.168.1.234:8080/chats"
    }
}
