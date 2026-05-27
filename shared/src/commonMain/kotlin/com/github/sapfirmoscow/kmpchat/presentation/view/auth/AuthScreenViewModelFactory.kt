package com.github.sapfirmoscow.kmpchat.presentation.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.usecase.LoginUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.RegisterUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

/**
 * Фабрика для создания [AuthScreenViewModel] через [ViewModelProvider].
 *
 * @param registerUseCase Use-case регистрации, передаваемый в создаваемый ViewModel.
 * @param loginUseCase    Use-case входа, передаваемый в создаваемый ViewModel.
 * @param navigator       Навигатор приложения.
 */
internal class AuthScreenViewModelFactory @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val navigator: Navigator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        AuthScreenViewModel(registerUseCase, loginUseCase, navigator) as T
}
