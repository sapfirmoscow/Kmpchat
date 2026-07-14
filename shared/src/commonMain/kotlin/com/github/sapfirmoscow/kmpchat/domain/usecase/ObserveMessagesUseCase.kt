package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.ChatWebSocketApi
import com.github.sapfirmoscow.kmpchat.domain.model.Message
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Use-case для наблюдения за новыми сообщениями через WebSocket.
 *
 * @param chatWebSocketApi API-клиент WebSocket.
 */
internal class ObserveMessagesUseCase @Inject constructor(
    private val chatWebSocketApi: ChatWebSocketApi
) {
    /**
     * Возвращает [Flow] новых сообщений для указанного чата.
     * Flow активен, пока активен коллектор.
     *
     * @param chatId Идентификатор чата для фильтрации.
     */
    operator fun invoke(chatId: String): Flow<Message> =
        chatWebSocketApi.observeMessages()
            .filter { it.chatId == chatId }
            .map { dto ->
                Message(
                    id = dto.id,
                    senderId = dto.senderId,
                    text = dto.text,
                    sentAt = dto.createdAt
                )
            }
}
