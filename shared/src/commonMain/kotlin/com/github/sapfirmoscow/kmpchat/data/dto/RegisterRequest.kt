package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

/**
 * Тело запроса на регистрацию нового пользователя.
 *
 * @property username Желаемое имя пользователя.
 * @property password Пароль в открытом виде (передаётся по HTTPS).
 */
@Serializable
internal data class RegisterRequest(
    val username: String,
    val password: String
)
