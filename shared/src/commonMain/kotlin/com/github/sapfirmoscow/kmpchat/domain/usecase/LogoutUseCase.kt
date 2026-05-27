package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject

/**
 * Use-case выхода из системы.
 *
 * Удаляет JWT-токен и данные текущего пользователя, завершая сессию.
 *
 * @param jwtTokenRepository    Репозиторий токена.
 * @param currentUserRepository Репозиторий данных текущего пользователя.
 */
internal class LogoutUseCase @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository,
    private val currentUserRepository: CurrentUserRepository
) {
    /** Завершает сессию: удаляет токен и данные пользователя. */
    operator fun invoke() {
        jwtTokenRepository.deleteToken()
        currentUserRepository.clearCurrentUser()
    }
}
