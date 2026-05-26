package com.github.sapfirmoscow.kmpchat.presentation.view

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}
