package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.GetChatApi
import com.github.sapfirmoscow.kmpchat.domain.model.Chat
import com.github.sapfirmoscow.kmpchat.domain.model.User
import dev.zacsweers.metro.Inject

/**
 * Use-case для загрузки списка чатов текущего пользователя.
 *
 * Преобразует DTO-ответ API в доменные модели [Chat] и [User].
 *
 * @param getChatApi API-клиент для получения чатов.
 */
internal class GetChatsUseCase @Inject constructor(
    private val getChatApi: GetChatApi
) {
    /**
     * Загружает и возвращает список чатов.
     *
     * @return Список доменных объектов [Chat].
     */
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