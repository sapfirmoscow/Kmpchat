package com.github.sapfirmoscow.kmpchat.presentation.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.usecase.RegisterUseCase
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

internal class AuthScreenViewModelFactory @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T = AuthScreenViewModel(registerUseCase) as T
}
