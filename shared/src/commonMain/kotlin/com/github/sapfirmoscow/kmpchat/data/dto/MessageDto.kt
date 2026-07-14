package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO одного сообщения.
 *
 * @property id        Уникальный идентификатор сообщения.
 * @property chatId    Идентификатор чата.
 * @property senderId  Идентификатор отправителя.
 * @property text      Текст сообщения.
 * @property createdAt Дата и время отправки (ISO-8601).
 */
@Serializable
internal data class MessageDto(
    val id: String,
    @SerialName("chat_id") val chatId: String,
    @SerialName("sender_id") val senderId: String,
    val text: String,
    @SerialName("created_at") val createdAt: String
)

/**
 * Ответ сервера со списком сообщений.
 *
 * @property messages Список сообщений.
 */
@Serializable
internal data class MessagesResponse(
    val messages: List<MessageDto>
)

/**
 * Тело запроса для отправки сообщения.
 *
 * @property text Текст сообщения (1–2000 символов).
 */
@Serializable
internal data class SendMessageRequest(
    val text: String
)
