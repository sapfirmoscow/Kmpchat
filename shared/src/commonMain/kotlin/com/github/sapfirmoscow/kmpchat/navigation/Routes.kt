package com.github.sapfirmoscow.kmpchat.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Конфигурация [SavedStateConfiguration] с поддержкой полиморфной сериализации
 * для sealed-интерфейса [Routes].
 */
@OptIn(ExperimentalSerializationApi::class)
val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Routes>()
        }
    }
}

/**
 * Навигационные маршруты приложения.
 *
 * Реализует [NavKey] для использования с Navigation3 и поддерживает
 * сериализацию через kotlinx.serialization.
 */
@Serializable
internal sealed interface Routes : NavKey {
    /** Экран аутентификации (вход / регистрация). */
    @Serializable
    data object Auth : Routes

    /** Экран со списком чатов текущего пользователя. */
    @Serializable
    data object ChatsList : Routes
}

