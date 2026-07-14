package com.github.sapfirmoscow.kmpchat.presentation.view.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sapfirmoscow.kmpchat.domain.model.Message
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetMessagesUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.ObserveMessagesUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.SendMessageUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
 * @param getMessagesUseCase    Use-case загрузки истории сообщений.
 * @param sendMessageUseCase    Use-case отправки сообщения.
 * @param observeMessagesUseCase Use-case наблюдения за новыми сообщениями через WebSocket.
 * @param currentUserRepository Репозиторий текущего пользователя.
 * @param navigator             Навигатор приложения.
 */
internal class ChatViewModel(
    private val chatId: String,
    chatName: String,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
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

    init {
        loadMessages()
        observeNewMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getMessagesUseCase(chatId) }
                .onSuccess { messages -> _uiState.update { it.copy(messages = messages, isLoading = false) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    private fun observeNewMessages() {
        viewModelScope.launch {
            observeMessagesUseCase(chatId).collect { message ->
                _uiState.update { state ->
                    if (state.messages.any { it.id == message.id }) state
                    else state.copy(messages = state.messages + message)
                }
            }
        }
    }

    /** Возврат к списку чатов. */
    fun goBack() = navigator.goBack()

    /** Обновляет текст в поле ввода. */
    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    /** Отправляет сообщение и сбрасывает поле ввода. */
    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return
        _uiState.update { it.copy(inputText = "") }
        viewModelScope.launch {
            runCatching { sendMessageUseCase(chatId, text) }
                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }
}
