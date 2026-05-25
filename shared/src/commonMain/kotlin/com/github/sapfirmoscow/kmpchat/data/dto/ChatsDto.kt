package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChatsResponse(
    val chats: List<ChatDto>
)

@Serializable
internal data class ChatDto(
    val id: String,
    val members: List<UserDto>,
    @SerialName("created_at")
    val createdAt: String
)