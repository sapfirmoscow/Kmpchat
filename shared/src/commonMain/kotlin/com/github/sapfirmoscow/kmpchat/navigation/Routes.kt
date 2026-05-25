package com.github.sapfirmoscow.kmpchat.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Routes>()
        }
    }
}

@Serializable
internal sealed interface Routes : NavKey {
    @Serializable
    data object Auth : Routes
    @Serializable
    data object ChatsList : Routes
}

