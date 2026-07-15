package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.client.ApiConfig
import com.github.sapfirmoscow.kmpchat.data.dto.FindUserResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodeURLPathPart

/**
 * Клиент API для поиска пользователя по точному username.
 *
 * Поиск на сервере намеренно точный, а не по подстроке, поэтому результат —
 * либо один пользователь, либо ничего.
 *
 * @param httpClient Настроенный HTTP-клиент Ktor (с Bearer-авторизацией).
 * @param apiConfig  Адреса backend-сервера.
 */
internal class FindUserApi @Inject constructor(
    private val httpClient: HttpClient,
    private val apiConfig: ApiConfig
) {
    /**
     * Ищет пользователя по точному совпадению [username].
     *
     * @param username Имя пользователя (регистр не важен — сервер нормализует).
     * @return [FindUserResponse], либо `null`, если пользователь не найден.
     * @throws IllegalStateException при превышении лимита запросов или иной ошибке сервера.
     */
    suspend fun findByUsername(username: String): FindUserResponse? {
        val response = httpClient.get("${apiConfig.baseUrl}/users/by-username/${username.encodeURLPathPart()}")

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            HttpStatusCode.TooManyRequests ->
                error("Слишком много запросов. Попробуйте через минуту")
            else -> error("Не удалось выполнить поиск (${response.status.value})")
        }
    }
}
