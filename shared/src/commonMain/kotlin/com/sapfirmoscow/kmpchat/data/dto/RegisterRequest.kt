package com.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterRequest(
    val username: String,
    val password: String
)
