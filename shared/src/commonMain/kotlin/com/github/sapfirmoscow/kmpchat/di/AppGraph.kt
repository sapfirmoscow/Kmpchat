package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes

@DependencyGraph
interface AppGraph {

    val encryptedStorage: EncryptedStorage

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes platformComponent: PlatformComponent) : AppGraph
    }
}