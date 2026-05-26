package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.lifecycle.ViewModel
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AppViewModel @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        _authState.value = if (jwtTokenRepository.getToken() != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun onLoginSuccess() {
        _authState.value = AuthState.Authenticated
    }

    fun logout() {
        jwtTokenRepository.deleteToken()
        _authState.value = AuthState.Unauthenticated
    }
}
