package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.data.local.JvmEncryptedStorage
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

/**
 * Desktop (JVM)-специфичный граф зависимостей (Metro DI).
 *
 * Реализует [PlatformComponent], предоставляя [JvmEncryptedStorage].
 * Создаётся через [Factory] без дополнительных параметров.
 */
@DependencyGraph
interface DesktopAppGraph : PlatformComponent {

    override val encryptedStorage: EncryptedStorage

    @Provides
    fun provideEncryptedStorage(): EncryptedStorage =
        JvmEncryptedStorage()

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(): DesktopAppGraph
    }
}