package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val token: String
)
