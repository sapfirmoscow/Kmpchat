package com.github.sapfirmoscow.kmpchat.data.remote

import com.github.sapfirmoscow.kmpchat.data.dto.RegisterRequest
import com.github.sapfirmoscow.kmpchat.data.dto.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class AuthApi(
    private val httpClient: HttpClient
) {
    suspend fun register(username: String, password: String): RegisterResponse {
        return httpClient.post(REGISTER_URL) {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username, password))
        }.body()
    }

    private companion object {
        const val REGISTER_URL = "http://192.168.1.234:8080/register"
    }
}