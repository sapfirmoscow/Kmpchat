package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.GetMessagesApi
import com.github.sapfirmoscow.kmpchat.domain.model.Message
import dev.zacsweers.metro.Inject

/**
 * Use-case для загрузки истории сообщений чата.
 *
 * @param getMessagesApi API-клиент для получения сообщений.
 */
internal class GetMessagesUseCase @Inject constructor(
    private val getMessagesApi: GetMessagesApi
) {
    /**
     * Загружает и возвращает список сообщений для указанного чата.
     *
     * @param chatId Идентификатор чата.
     * @return Список доменных объектов [Message].
     */
    suspend operator fun invoke(chatId: String): List<Message> {
        return getMessagesApi.getMessages(chatId).messages.map { dto ->
            Message(
                id = dto.id,
                senderId = dto.senderId,
                text = dto.text,
                sentAt = dto.createdAt
            )
        }
    }
}
