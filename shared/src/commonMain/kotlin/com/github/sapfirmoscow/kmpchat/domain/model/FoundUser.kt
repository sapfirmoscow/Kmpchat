package com.github.sapfirmoscow.kmpchat.domain.model

/**
 * Пользователь, найденный поиском по username.
 *
 * Отдельная модель, а не [User]: поиск намеренно отдаёт минимум данных —
 * только идентификатор и имя, даты регистрации в ответе нет.
 *
 * @property id       Уникальный идентификатор пользователя (нужен как `member_id` при создании чата).
 * @property username Имя пользователя.
 */
data class FoundUser(
    val id: String,
    val username: String
)
