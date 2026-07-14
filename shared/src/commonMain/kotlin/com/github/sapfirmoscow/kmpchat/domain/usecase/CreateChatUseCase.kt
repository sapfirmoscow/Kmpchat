package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.CreateChatApi
import com.github.sapfirmoscow.kmpchat.domain.model.Chat
import com.github.sapfirmoscow.kmpchat.domain.model.User
import dev.zacsweers.metro.Inject

/**
 * Use-case для создания DM-чата с другим пользователем.
 *
 * @param createChatApi API-клиент для создания чата.
 */
internal class CreateChatUseCase @Inject constructor(
    private val createChatApi: CreateChatApi
) {
    /**
     * Создаёт или возвращает существующий DM-чат с указанным пользователем.
     *
     * @param memberId Идентификатор собеседника.
     * @return Доменный объект [Chat].
     */
    suspend operator fun invoke(memberId: String): Chat {
        val dto = createChatApi.createChat(memberId)
        return Chat(
            id = dto.id,
            members = dto.members.map { User(it.id, it.username, it.createdAt) },
            createdAt = dto.createdAt
        )
    }
}
