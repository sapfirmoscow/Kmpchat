package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ сервера со списком чатов пользователя.
 *
 * @property chats Список чатов.
 */
@Serializable
internal data class ChatsResponse(
    val chats: List<ChatDto>
)

/**
 * DTO отдельного чата.
 *
 * @property id Уникальный идентификатор чата.
 * @property members Список участников чата.
 * @property createdAt Дата и время создания чата (ISO-8601).
 */
@Serializable
internal data class ChatDto(
    val id: String,
    val members: List<UserDto>,
    @SerialName("created_at")
    val createdAt: String
)