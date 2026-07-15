package com.github.sapfirmoscow.kmpchat.presentation.view.common

import com.github.sapfirmoscow.kmpchat.domain.model.Chat

/**
 * Имя чата с точки зрения пользователя [currentUserId].
 *
 * Живёт в presentation, а не рядом с [Chat], потому что имя — не свойство чата:
 * оно зависит от того, кто смотрит, и один и тот же чат у разных участников
 * называется по-разному. Поэтому вычисляется при сборке состояния экрана.
 *
 * `GET /chats` отдаёт в [Chat.members] всех участников, включая смотрящего,
 * поэтому его нужно исключить — иначе alice увидит свой чат подписанным
 * «alice, bob». Полагаться на то, что сервер спрячет нас сам, нельзя:
 * `POST /chats` создателя не возвращает, а `GET /chats` возвращает.
 */
internal fun Chat.titleFor(currentUserId: String): String =
    members.filter { it.id != currentUserId }
        .joinToString(", ") { it.username }
        // Участников кроме нас не осталось — показываем что есть, лишь бы строка не была пустой.
        .ifBlank { members.joinToString(", ") { it.username } }
