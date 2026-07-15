package com.github.sapfirmoscow.kmpchat.presentation.view.newchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sapfirmoscow.kmpchat.domain.model.FoundUser
import com.github.sapfirmoscow.kmpchat.domain.usecase.CreateChatUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.FindUserUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import com.github.sapfirmoscow.kmpchat.navigation.Routes
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI-состояние экрана создания нового чата.
 *
 * @property username       Текущий текст в поле ввода имени.
 * @property isSearching    `true`, пока идёт поиск пользователя.
 * @property foundUser      Найденный пользователь или `null`.
 * @property notFound       `true`, если поиск завершился и пользователь не найден.
 * @property isCreatingChat `true`, пока создаётся чат.
 * @property error          Сообщение об ошибке или `null`.
 */
data class NewChatUiState(
    val username: String = "",
    val isSearching: Boolean = false,
    val foundUser: FoundUser? = null,
    val notFound: Boolean = false,
    val isCreatingChat: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel экрана создания нового чата.
 *
 * Сценарий: пользователь вводит точный username собеседника, видит результат
 * поиска и подтверждает создание чата. Подтверждение нужно, чтобы не создать
 * переписку не с тем человеком.
 *
 * @param findUserUseCase   Use-case поиска пользователя по имени.
 * @param createChatUseCase Use-case создания DM-чата.
 * @param navigator         Навигатор приложения.
 */
internal class NewChatViewModel @Inject constructor(
    private val findUserUseCase: FindUserUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewChatUiState())

    /** Текущее UI-состояние экрана. */
    val uiState: StateFlow<NewChatUiState> = _uiState

    /** Обновляет поле ввода и сбрасывает результат предыдущего поиска. */
    fun onUsernameChange(text: String) {
        _uiState.update {
            it.copy(username = text, foundUser = null, notFound = false, error = null)
        }
    }

    /** Ищет пользователя по введённому имени. */
    fun search() {
        val username = _uiState.value.username.trim()
        if (username.isBlank() || _uiState.value.isSearching) return

        _uiState.update {
            it.copy(isSearching = true, foundUser = null, notFound = false, error = null)
        }

        viewModelScope.launch {
            runCatching { findUserUseCase(username) }
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(isSearching = false, foundUser = user, notFound = user == null)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSearching = false, error = e.message ?: "Не удалось выполнить поиск")
                    }
                }
        }
    }

    /**
     * Создаёт чат с найденным пользователем и открывает переписку.
     *
     * Экран поиска убирается из стека, поэтому возврат из чата ведёт к списку чатов.
     */
    fun startChat() {
        val user = _uiState.value.foundUser ?: return
        if (_uiState.value.isCreatingChat) return

        _uiState.update { it.copy(isCreatingChat = true, error = null) }

        viewModelScope.launch {
            runCatching { createChatUseCase(user.id) }
                .onSuccess { chat ->
                    navigator.goBack()
                    navigator.navigate(
                        Routes.Chat(
                            chatId = chat.id,
                            chatName = chat.members.joinToString(", ") { it.username }
                        )
                    )
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isCreatingChat = false, error = e.message ?: "Не удалось создать чат")
                    }
                }
        }
    }

    /** Возврат к списку чатов. */
    fun goBack() = navigator.goBack()
}
