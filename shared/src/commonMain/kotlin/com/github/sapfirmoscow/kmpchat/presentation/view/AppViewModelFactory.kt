package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

/**
 * Фабрика для создания [AppViewModel] через [ViewModelProvider].
 *
 * @param jwtTokenRepository    Репозиторий токена, передаваемый в создаваемый ViewModel.
 * @param currentUserRepository Репозиторий пользователя, передаваемый в создаваемый ViewModel.
 */
internal class AppViewModelFactory @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository,
    private val currentUserRepository: CurrentUserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        AppViewModel(jwtTokenRepository, currentUserRepository) as T
}
