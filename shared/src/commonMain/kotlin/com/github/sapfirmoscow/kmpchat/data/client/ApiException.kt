package com.github.sapfirmoscow.kmpchat.data.client

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Ошибка, которую вернул сервер.
 *
 * [message] — текст для показа пользователю (по-русски); исходный текст сервера
 * лежит в [serverMessage] и годится для логов.
 *
 * @property status            HTTP-статус ответа.
 * @property serverMessage     Поле `error` из тела ответа, если удалось его прочитать.
 * @property retryAfterSeconds Значение заголовка `Retry-After` (для 429), если есть.
 */
internal class ApiException(
    val status: HttpStatusCode,
    val serverMessage: String?,
    val retryAfterSeconds: Long?,
    message: String
) : Exception(message)

/** Тело ошибок API: `{"error": "..."}`. */
@Serializable
private data class ErrorResponse(val error: String)

private val errorJson = Json { ignoreUnknownKeys = true }

/**
 * Возвращает тело успешного ответа, иначе бросает [ApiException].
 *
 * @param describe Переопределяет текст ошибки для конкретного эндпоинта
 *                 (например, 401 на `/login` — это неверный пароль, а не истёкшая сессия).
 *                 Если вернула `null`, используется общий текст по статусу.
 */
internal suspend inline fun <reified T> HttpResponse.bodyOrThrow(
    noinline describe: (ApiException) -> String? = { null }
): T {
    if (status.isSuccess()) return body()
    throw toApiException(describe)
}

/** Собирает [ApiException] из неуспешного ответа. */
internal suspend fun HttpResponse.toApiException(
    describe: (ApiException) -> String? = { null }
): ApiException {
    val serverMessage = runCatching {
        errorJson.decodeFromString<ErrorResponse>(bodyAsText()).error
    }.getOrNull()
    val retryAfter = headers[HttpHeaders.RetryAfter]?.trim()?.toLongOrNull()

    val raw = ApiException(status, serverMessage, retryAfter, message = "")
    val message = describe(raw) ?: defaultMessage(raw)
    return ApiException(status, serverMessage, retryAfter, message)
}

private fun defaultMessage(e: ApiException): String = when (e.status) {
    HttpStatusCode.TooManyRequests -> tooManyRequestsMessage(e.retryAfterSeconds)
    HttpStatusCode.Unauthorized -> "Сессия истекла, войдите снова"
    HttpStatusCode.Forbidden -> "Нет доступа"
    HttpStatusCode.PayloadTooLarge -> "Слишком большой запрос"
    else -> if (e.status.value >= 500) {
        "Ошибка сервера (${e.status.value}). Попробуйте позже"
    } else {
        e.serverMessage ?: "Ошибка запроса (${e.status.value})"
    }
}

/** Текст для 429 с учётом `Retry-After`. */
internal fun tooManyRequestsMessage(retryAfterSeconds: Long?): String =
    if (retryAfterSeconds != null && retryAfterSeconds > 0) {
        "Слишком много запросов. Попробуйте через $retryAfterSeconds с"
    } else {
        "Слишком много запросов. Попробуйте позже"
    }
