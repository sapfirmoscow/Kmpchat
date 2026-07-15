package com.github.sapfirmoscow.kmpchat.presentation.view.newchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.repository.CurrentUserRepository
import com.github.sapfirmoscow.kmpchat.domain.usecase.CreateChatUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.FindUserUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

/**
 * Фабрика для создания [NewChatViewModel] через [ViewModelProvider].
 *
 * @param findUserUseCase       Use-case поиска пользователя, передаваемый в создаваемый ViewModel.
 * @param createChatUseCase     Use-case создания чата, передаваемый в создаваемый ViewModel.
 * @param currentUserRepository Репозиторий текущего пользователя.
 * @param navigator             Навигатор приложения.
 */
internal class NewChatViewModelFactory @Inject constructor(
    private val findUserUseCase: FindUserUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val currentUserRepository: CurrentUserRepository,
    private val navigator: Navigator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        NewChatViewModel(findUserUseCase, createChatUseCase, currentUserRepository, navigator) as T
}
