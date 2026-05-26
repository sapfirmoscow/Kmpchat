package com.github.sapfirmoscow.kmpchat.presentation.view

/**
 * Состояние аутентификации пользователя.
 *
 * Используется [AppViewModel] для управления навигацией между
 * экраном входа и основным контентом приложения.
 */
sealed interface AuthState {
    /** Начальное состояние: идёт проверка наличия сохранённого токена. */
    data object Loading : AuthState

    /** Пользователь авторизован; токен присутствует в хранилище. */
    data object Authenticated : AuthState

    /** Пользователь не авторизован; токен отсутствует или был удалён. */
    data object Unauthenticated : AuthState
}
