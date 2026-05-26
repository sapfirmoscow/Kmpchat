package com.github.sapfirmoscow.kmpchat.domain.model

/**
 * Доменная модель чата.
 *
 * @property id        Уникальный идентификатор чата.
 * @property members   Список участников чата.
 * @property createdAt Дата и время создания чата (ISO-8601).
 */
data class Chat(
    val id: String,
    val members: List<User>,
    val createdAt: String
)

/**
 * Доменная модель пользователя.
 *
 * @property id        Уникальный идентификатор пользователя.
 * @property username  Имя пользователя.
 * @property createdAt Дата и время регистрации (ISO-8601).
 */
data class User(
    val id: String,
    val username: String,
    val createdAt: String
)