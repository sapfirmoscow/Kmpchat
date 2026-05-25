package com.github.sapfirmoscow.kmpchat.presentation.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sapfirmoscow.kmpchat.domain.usecase.LoginUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.RegisterUseCase
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

internal class AuthScreenViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(username: String, password: String) {
        launchAuth { registerUseCase(username, password) }
    }

    fun login(username: String, password: String) {
        launchAuth { loginUseCase(username, password) }
    }

    private fun launchAuth(action: suspend () -> Unit) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { action() }
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = e.message ?: "Ошибка авторизации")
            }
        }
    }
}
