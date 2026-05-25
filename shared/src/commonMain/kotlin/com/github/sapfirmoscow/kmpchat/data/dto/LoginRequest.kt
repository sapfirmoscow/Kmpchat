package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String
)
