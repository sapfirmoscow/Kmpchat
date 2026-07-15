package com.github.sapfirmoscow.kmpchat.domain.usecase

import com.github.sapfirmoscow.kmpchat.data.remote.FindUserApi
import com.github.sapfirmoscow.kmpchat.domain.model.FoundUser
import dev.zacsweers.metro.Inject

/**
 * Use-case поиска пользователя по username — первый шаг создания DM-чата.
 *
 * @param findUserApi API-клиент поиска пользователей.
 */
internal class FindUserUseCase @Inject constructor(
    private val findUserApi: FindUserApi
) {
    /**
     * Ищет пользователя по точному совпадению имени.
     *
     * @param username Имя пользователя.
     * @return [FoundUser], либо `null`, если такого пользователя нет.
     */
    suspend operator fun invoke(username: String): FoundUser? {
        val dto = findUserApi.findByUsername(username.trim()) ?: return null
        return FoundUser(id = dto.id, username = dto.username)
    }
}
