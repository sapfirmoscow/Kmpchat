package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

/**
 * Ответ сервера на успешный вход в систему.
 *
 * @property token JWT-токен для авторизации последующих запросов.
 */
@Serializable
internal data class LoginResponse(
    val token: String
)
