package com.github.sapfirmoscow.kmpchat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Тело запроса для создания DM-чата.
 *
 * @property memberId Идентификатор пользователя, с которым создаётся чат.
 */
@Serializable
internal data class CreateChatRequest(
    @SerialName("member_id") val memberId: String
)
