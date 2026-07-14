package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.SendMessageApi
import com.github.sapfirmoscow.kmpchat.domain.model.Message
import dev.zacsweers.metro.Inject

/**
 * Use-case для отправки сообщения в чат.
 *
 * @param sendMessageApi API-клиент для отправки сообщений.
 */
internal class SendMessageUseCase @Inject constructor(
    private val sendMessageApi: SendMessageApi
) {
    /**
     * Отправляет сообщение и возвращает сохранённый объект.
     *
     * @param chatId Идентификатор чата.
     * @param text   Текст сообщения.
     * @return Доменный объект [Message].
     */
    suspend operator fun invoke(chatId: String, text: String): Message {
        val dto = sendMessageApi.sendMessage(chatId, text)
        return Message(
            id = dto.id,
            senderId = dto.senderId,
            text = dto.text,
            sentAt = dto.createdAt
        )
    }
}
