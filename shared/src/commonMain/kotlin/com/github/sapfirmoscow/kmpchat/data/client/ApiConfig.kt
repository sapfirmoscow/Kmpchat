package com.github.sapfirmoscow.kmpchat.data.client

/**
 * Адреса backend-сервера.
 *
 * Единственное место, где задаётся хост: все API-клиенты строят свои пути
 * от [baseUrl], WebSocket-клиент использует [wsUrl].
 *
 * @property baseUrl Базовый URL REST API, без завершающего слэша (например `http://host:8080`).
 * @property wsUrl   Полный URL WebSocket-эндпоинта (например `ws://host:8080/ws`).
 */
internal data class ApiConfig(
    val baseUrl: String,
    val wsUrl: String
) {
    companion object {
        /** Хост сервера разработки. Менять адрес нужно здесь. */
        private const val DEFAULT_HOST = "192.168.1.234:8080"

        /** Конфигурация по умолчанию, указывающая на [DEFAULT_HOST]. */
        val Default = ApiConfig(
            baseUrl = "http://$DEFAULT_HOST",
            wsUrl = "ws://$DEFAULT_HOST/ws"
        )
    }
}
