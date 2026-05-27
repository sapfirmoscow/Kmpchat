package com.github.sapfirmoscow.kmpchat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Навигационные маршруты приложения.
 *
 * Реализует [NavKey] для использования с Navigation3 и поддерживает
 * сериализацию через kotlinx.serialization.
 *
 * @property requiresLogin `true`, если переход на маршрут разрешён только
 * авторизованному пользователю. Используется [Navigator] для условного
 * редиректа на [Auth].
 */
@Serializable
internal sealed interface Routes : NavKey {
    val requiresLogin: Boolean

    /** Экран аутентификации (вход / регистрация). */
    @Serializable
    data object Auth : Routes {
        override val requiresLogin: Boolean = false
    }

    /** Экран со списком чатов текущего пользователя. */
    @Serializable
    data object ChatsList : Routes {
        override val requiresLogin: Boolean = true
    }
}

