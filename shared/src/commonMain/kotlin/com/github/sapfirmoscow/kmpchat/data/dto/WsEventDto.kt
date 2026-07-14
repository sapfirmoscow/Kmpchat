package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

/**
 * WebSocket-событие от сервера.
 *
 * @property type Тип события (например, `"message.new"`).
 * @property data Данные сообщения.
 */
@Serializable
internal data class WsEventDto(
    val type: String,
    val data: MessageDto
)
