package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.dto.ChatsResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class GetChatApi @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getChats(): ChatsResponse {
        return httpClient.get(CHATS_URL).body<ChatsResponse>()
    }

    private companion object {
        const val CHATS_URL = "http://192.168.1.234:8080/chats"
    }
}