package com.github.sapfirmoscow.kmpchat.presentation.view.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetMessagesUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.ObserveMessagesUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.SendMessageUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

/**
 * Фабрика для создания [ChatViewModel].
 *
 * Инжектируется через DI; конкретный [chatId] и [chatName] передаются
 * при создании через [create], поскольку они известны только в момент навигации.
 *
 * @param getMessagesUseCase     Use-case загрузки истории сообщений.
 * @param sendMessageUseCase     Use-case отправки сообщения.
 * @param observeMessagesUseCase Use-case наблюдения за новыми сообщениями через WebSocket.
 * @param currentUserRepository  Репозиторий текущего пользователя.
 * @param navigator              Навигатор приложения.
 */
internal class ChatViewModelFactory @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val currentUserRepository: CurrentUserRepository,
    private val navigator: Navigator
) {
    fun create(chatId: String, chatName: String): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
                ChatViewModel(
                    chatId = chatId,
                    chatName = chatName,
                    getMessagesUseCase = getMessagesUseCase,
                    sendMessageUseCase = sendMessageUseCase,
                    observeMessagesUseCase = observeMessagesUseCase,
                    currentUserRepository = currentUserRepository,
                    navigator = navigator
                ) as T
        }
}
