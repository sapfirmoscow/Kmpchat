package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterResponse(
    val token: String,
    val user: UserDto
)

@Serializable
internal data class UserDto(
    val id: String,
    val username: String,
    @SerialName("created_at")
    val createdAt: String
)
