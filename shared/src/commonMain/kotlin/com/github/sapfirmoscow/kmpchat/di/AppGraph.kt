package com.github.sapfirmoscow.kmpchat.di

import androidx.lifecycle.ViewModelProvider
import com.github.sapfirmoscow.kmpchat.data.client.createHttpClient
import com.github.sapfirmoscow.kmpchat.data.local.EncryptedStorage
import com.github.sapfirmoscow.kmpchat.data.repository.JWTTokenRepositoryImpl
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import com.github.sapfirmoscow.kmpchat.domain.usecase.RegisterUseCase
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModelFactory
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient

@DependencyGraph
abstract class AppGraph {

    abstract val encryptedStorage: EncryptedStorage
    abstract val authScreenViewModelFactory: ViewModelProvider.Factory

    @Provides
    fun provideHttpClient(
        jwtTokenRepository: JWTTokenRepository
    ): HttpClient = createHttpClient(jwtTokenRepository)

    @Provides
    fun provideJWTTokenRepository(): JWTTokenRepository = JWTTokenRepositoryImpl(encryptedStorage)

    @Provides
    internal fun provideViewModelFactory(registerUseCase: RegisterUseCase): ViewModelProvider.Factory =
        AuthScreenViewModelFactory(registerUseCase)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes platformComponent: PlatformComponent): AppGraph
    }
}