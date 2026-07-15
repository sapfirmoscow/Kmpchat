package com.github.sapfirmoscow.kmpchat.di

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.client.createHttpClient
import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.data.repository.CurrentUserRepositoryImpl
import com.github.sapfirmoscow.kmpchat.data.repository.JWTTokenRepositoryImpl
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import com.github.sapfirmoscow.kmpchat.domain.session.SessionManager
import com.github.sapfirmoscow.kmpchat.domain.usecase.LogoutUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModelFactory
import com.github.sapfirmoscow.kmpchat.presentation.view.chat.ChatViewModelFactory
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsViewModelFactory
import com.github.sapfirmoscow.kmpchat.presentation.view.newchat.NewChatViewModelFactory
import dev.zacsweers.metro.AppScope
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
@DependencyGraph(AppScope::class)
abstract class AppGraph {

    abstract val encryptedStorage: EncryptedStorage
    internal abstract val navigator: Navigator
    internal abstract val authScreenViewModelFactory: AuthScreenViewModelFactory
    internal abstract val chatsViewModelFactory: ChatsViewModelFactory
    internal abstract val chatViewModelFactory: ChatViewModelFactory
    internal abstract val newChatViewModelFactory: NewChatViewModelFactory
    internal abstract val jwtTokenRepository: JWTTokenRepository
    internal abstract val sessionManager: SessionManager
    internal abstract val logoutUseCase: LogoutUseCase

    @Provides
    internal fun provideApiConfig(): ApiConfig = ApiConfig.Default

    @Provides
    internal fun provideHttpClient(
        jwtTokenRepository: JWTTokenRepository,
        sessionManager: SessionManager
    ): HttpClient = createHttpClient(jwtTokenRepository, sessionManager)

    @Provides
    fun provideJWTTokenRepository(): JWTTokenRepository = JWTTokenRepositoryImpl(encryptedStorage)

    @Provides
    fun provideCurrentUserRepository(): CurrentUserRepository = CurrentUserRepositoryImpl(encryptedStorage)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes platformComponent: PlatformComponent): AppGraph
    }
}
