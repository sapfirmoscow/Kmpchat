package com.github.sapfirmoscow.kmpchat.presentation.view.chat

import androidx.lifecycle.ViewModel
import com.github.sapfirmoscow.kmpchat.domain.model.Message
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * UI-состояние экрана чата.
 *
 * @property chatName       Имя собеседника (или участников).
 * @property messages       Список сообщений в чате.
 * @property inputText      Текущий текст в поле ввода.
 * @property currentUserId  Идентификатор текущего пользователя для определения направления пузырька.
 * @property isLoading      `true`, когда идёт загрузка сообщений.
 * @property error          Сообщение об ошибке или `null`.
 */
data class ChatUiState(
    val chatName: String = "",
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val currentUserId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel экрана чата.
 *
 * @param chatId                Идентификатор чата.
 * @param chatName              Отображаемое имя чата (передаётся из маршрута).
 * @param currentUserRepository Репозиторий текущего пользователя.
 * @param navigator             Навигатор приложения.
 */
internal class ChatViewModel(
    @Suppress("UNUSED_PARAMETER") private val chatId: String,
    chatName: String,
    private val currentUserRepository: CurrentUserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState(
            chatName = chatName,
            currentUserId = currentUserRepository.getCurrentUser()?.id.orEmpty()
        )
    )

    /** Текущее UI-состояние экрана. */
    val uiState: StateFlow<ChatUiState> = _uiState

    /** Возврат к списку чатов. */
    fun goBack() = navigator.goBack()

    /** Обновляет текст в поле ввода. */
    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    /** Отправляет сообщение и сбрасывает поле ввода. */
    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return
        // TODO: отправить через API
        _uiState.value = _uiState.value.copy(inputText = "")
    }
}
