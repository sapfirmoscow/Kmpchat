package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

/**
 * Тело запроса для входа в систему.
 *
 * @property username Имя пользователя.
 * @property password Пароль в открытом виде (передаётся по HTTPS).
 */
@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String
)
