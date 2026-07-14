package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.dto.ChatsResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Клиент API для получения списка чатов.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor (с Bearer-авторизацией).
 * @param apiConfig  Адреса backend-сервера.
 */
internal class GetChatApi @Inject constructor(
    private val httpClient: HttpClient,
    private val apiConfig: ApiConfig
) {
    /**
     * Загружает список чатов текущего авторизованного пользователя.
     *
     * @return [ChatsResponse] со списком чатов.
     */
    suspend fun getChats(): ChatsResponse {
        return httpClient.get("${apiConfig.baseUrl}/chats").body<ChatsResponse>()
    }
}