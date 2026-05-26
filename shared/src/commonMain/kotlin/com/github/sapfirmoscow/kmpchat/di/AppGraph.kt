package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.client.createHttpClient
import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.data.repository.JWTTokenRepositoryImpl
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import com.github.sapfirmoscow.kmpchat.presentation.view.AppViewModelFactory
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModelFactory
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsViewModelFactory
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient

/**
 * Корневой граф зависимостей приложения (Metro DI).
 *
 * Предоставляет общие зависимости: HTTP-клиент, репозиторий токенов
 * и фабрики ViewModel для всех экранов. Создаётся через [Factory]
 * с подключением платформенного [PlatformComponent].
 */
@DependencyGraph
abstract class AppGraph {

    abstract val encryptedStorage: EncryptedStorage
    internal abstract val appViewModelFactory: AppViewModelFactory
    internal abstract val authScreenViewModelFactory: AuthScreenViewModelFactory
    internal abstract val chatsViewModelFactory: ChatsViewModelFactory
    internal abstract val jwtTokenRepository: JWTTokenRepository

    @Provides
    fun provideHttpClient(
        jwtTokenRepository: JWTTokenRepository
    ): HttpClient = createHttpClient(jwtTokenRepository)

    @Provides
    fun provideJWTTokenRepository(): JWTTokenRepository = JWTTokenRepositoryImpl(encryptedStorage)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes platformComponent: PlatformComponent): AppGraph
    }
}
