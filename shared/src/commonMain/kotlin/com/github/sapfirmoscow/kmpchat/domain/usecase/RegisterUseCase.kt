package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.AuthApi
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject

internal class RegisterUseCase @Inject constructor(
    private val authApi: AuthApi,
    private val jwtTokenRepository: JWTTokenRepository
) {
    suspend operator fun invoke(username: String, password: String) {
        val response = authApi.register(username, password)
        jwtTokenRepository.saveToken(response.token)
    }
}