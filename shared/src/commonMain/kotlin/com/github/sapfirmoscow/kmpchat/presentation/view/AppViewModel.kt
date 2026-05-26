package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.lifecycle.ViewModel
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Корневой ViewModel приложения, управляющий состоянием аутентификации.
 *
 * При создании синхронно определяет начальное состояние по наличию JWT-токена.
 *
 * @param jwtTokenRepository    Репозиторий для чтения и удаления токена.
 * @param currentUserRepository Репозиторий данных текущего пользователя.
 */
internal class AppViewModel @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository,
    private val currentUserRepository: CurrentUserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)

    /** Текущее состояние авторизации пользователя. */
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        _authState.value = if (jwtTokenRepository.getToken() != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    /** Переводит состояние в [AuthState.Authenticated] после успешного входа. */
    fun onLoginSuccess() {
        _authState.value = AuthState.Authenticated
    }

    /** Удаляет токен и данные пользователя, переводит состояние в [AuthState.Unauthenticated]. */
    fun logout() {
        jwtTokenRepository.deleteToken()
        currentUserRepository.clearCurrentUser()
        _authState.value = AuthState.Unauthenticated
    }
}
