package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.GetChatApi
import com.github.sapfirmoscow.kmpchat.domain.model.Chat
import com.github.sapfirmoscow.kmpchat.domain.model.User
import dev.zacsweers.metro.Inject

internal class GetChatsUseCase @Inject constructor(
    private val getChatApi: GetChatApi
) {
    suspend operator fun invoke(): List<Chat> {
        return getChatApi.getChats().chats.map { chatDto ->
            Chat(
                id = chatDto.id,
                members = chatDto.members.map { userDto ->
                    User(
                        id = userDto.id,
                        username = userDto.username,
                        createdAt = userDto.createdAt
                    )
                },
                createdAt = chatDto.createdAt
            )
        }
    }
}