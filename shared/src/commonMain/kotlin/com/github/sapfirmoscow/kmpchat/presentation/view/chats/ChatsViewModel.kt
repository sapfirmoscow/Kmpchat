package com.github.sapfirmoscow.kmpchat.presentation.view.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sapfirmoscow.kmpchat.domain.model.Chat
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetChatsUseCase
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatsUiState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val error: String? = null
)

internal class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState(isLoading = true))
    val uiState: StateFlow<ChatsUiState> = _uiState

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                val chats = withContext(Dispatchers.IO) { getChatsUseCase() }
                _uiState.value = ChatsUiState(chats = chats)
            } catch (e: Exception) {
                _uiState.value = ChatsUiState(error = e.message ?: "Ошибка загрузки")
            }
        }
    }
}