package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.Serializable

/**
 * Ответ сервера на поиск пользователя по username.
 *
 * Содержит только минимально необходимые поля — сервер намеренно
 * не отдаёт в поиске дату регистрации.
 *
 * @property id       Уникальный идентификатор пользователя.
 * @property username Имя пользователя.
 */
@Serializable
internal data class FindUserResponse(
    val id: String,
    val username: String
)
