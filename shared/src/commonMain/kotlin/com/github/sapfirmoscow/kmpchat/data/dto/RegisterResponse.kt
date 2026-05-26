package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ сервера на успешную регистрацию.
 *
 * @property token JWT-токен для последующих запросов.
 * @property user Данные зарегистрированного пользователя.
 */
@Serializable
internal data class RegisterResponse(
    val token: String,
    val user: UserDto
)

/**
 * DTO пользователя, возвращаемый API.
 *
 * @property id Уникальный идентификатор пользователя.
 * @property username Имя пользователя.
 * @property createdAt Дата и время создания аккаунта (ISO-8601).
 */
@Serializable
internal data class UserDto(
    val id: String,
    val username: String,
    @SerialName("created_at")
    val createdAt: String
)
