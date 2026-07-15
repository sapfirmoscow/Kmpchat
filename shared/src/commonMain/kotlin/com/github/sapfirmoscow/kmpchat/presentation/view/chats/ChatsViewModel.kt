package com.github.sapfirmoscow.kmpchat.presentation.view.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetChatsUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.LogoutUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import com.github.sapfirmoscow.kmpchat.navigation.Routes
import com.github.sapfirmoscow.kmpchat.presentation.view.common.titleFor
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Строка списка чатов — уже с готовым именем.
 *
 * Имя вычисляется здесь, а не на экране, потому что зависит от текущего
 * пользователя (см. [titleFor]), а экран о нём знать не обязан.
 *
 * @property id   Идентификатор чата.
 * @property name Имя чата с точки зрения текущего пользователя.
 */
data class ChatListItem(
    val id: String,
    val name: String
)

/**
 * UI-состояние экрана списка чатов.
 *
 * @property isLoading `true`, когда идёт загрузка данных с сервера.
 * @property chats     Загруженный список чатов.
 * @property error     Сообщение об ошибке или `null`, если ошибки нет.
 */
data class ChatsUiState(
    val isLoading: Boolean = false,
    val chats: List<ChatListItem> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel экрана списка чатов.
 *
 * Автоматически загружает чаты при создании через [GetChatsUseCase]
 * и обрабатывает действия экрана (выход из системы).
 *
 * @param getChatsUseCase       Use-case для получения списка чатов.
 * @param logoutUseCase         Use-case выхода из системы.
 * @param currentUserRepository Репозиторий текущего пользователя — нужен, чтобы
 *                              исключить его из имени чата.
 * @param navigator             Навигатор приложения.
 */
internal class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val currentUserRepository: CurrentUserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState(isLoading = true))

    /** Текущее UI-состояние экрана. */
    val uiState: StateFlow<ChatsUiState> = _uiState

    init {
        loadChats()
    }

    /** Завершает сессию и возвращает на экран аутентификации. */
    fun logout() {
        logoutUseCase()
        navigator.replaceAll(Routes.Auth)
    }

    /** Открывает экран переписки для чата с идентификатором [chatId]. */
    fun openChat(chatId: String, chatName: String) {
        navigator.navigate(Routes.Chat(chatId = chatId, chatName = chatName))
    }

    /** Открывает экран создания нового чата. */
    fun openNewChat() {
        navigator.navigate(Routes.NewChat)
    }

    /** Повторяет загрузку списка чатов после ошибки. */
    fun retry() {
        _uiState.value = ChatsUiState(isLoading = true)
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                val chats = withContext(Dispatchers.IO) { getChatsUseCase() }
                val currentUserId = currentUserRepository.getCurrentUser()?.id.orEmpty()
                _uiState.value = ChatsUiState(
                    chats = chats.map { chat ->
                        ChatListItem(id = chat.id, name = chat.titleFor(currentUserId))
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ChatsUiState(error = e.message ?: "Ошибка загрузки")
            }
        }
    }
}