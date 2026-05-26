package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.repository.JWTTokenRepository
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

internal class AppViewModelFactory @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        AppViewModel(jwtTokenRepository) as T
}
