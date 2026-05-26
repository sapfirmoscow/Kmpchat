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

/**
 * UI-состояние экрана аутентификации.
 *
 * @property isLoading       `true`, когда выполняется асинхронный запрос.
 * @property isAuthenticated `true` после успешного входа или регистрации.
 * @property error           Сообщение об ошибке или `null`, если ошибки нет.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel экрана аутентификации.
 *
 * Управляет состоянием [AuthUiState] и делегирует логику входа/регистрации
 * соответствующим use-case.
 *
 * @param registerUseCase Use-case регистрации нового пользователя.
 * @param loginUseCase    Use-case входа существующего пользователя.
 */
internal class AuthScreenViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())

    /** Текущее UI-состояние экрана. */
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Запускает регистрацию пользователя.
     *
     * @param username Желаемое имя пользователя.
     * @param password Пароль.
     */
    fun register(username: String, password: String) {
        launchAuth { registerUseCase(username, password) }
    }

    /**
     * Запускает вход пользователя в систему.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     */
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
