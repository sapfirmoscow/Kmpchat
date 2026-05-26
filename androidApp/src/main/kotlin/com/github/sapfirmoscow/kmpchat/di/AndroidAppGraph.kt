package com.github.sapfirmoscow.kmpchat.di

import android.app.Application
import android.content.Context
import com.github.sapfirmoscow.kmpchat.data.local.AndroidEncryptedStorage
import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

/**
 * Android-специфичный граф зависимостей (Metro DI).
 *
 * Реализует [PlatformComponent], предоставляя [AndroidEncryptedStorage]
 * и [Context] приложения. Создаётся через [Factory] с передачей [Application].
 */
@DependencyGraph
interface AndroidAppGraph : PlatformComponent {

    override val encryptedStorage: EncryptedStorage

    @Provides
    fun provideContext(application: Application): Context = application

    @Provides
    fun provideEncryptedStorage(context: Context): EncryptedStorage =
        AndroidEncryptedStorage(context)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AndroidAppGraph
    }
}