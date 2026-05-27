package com.github.sapfirmoscow.kmpchat.presentation.view.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetChatsUseCase
import com.github.sapfirmoscow.kmpchat.domain.usecase.LogoutUseCase
import com.github.sapfirmoscow.kmpchat.navigation.Navigator
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

/**
 * Фабрика для создания [ChatsViewModel] через [ViewModelProvider].
 *
 * @param getChatsUseCase Use-case загрузки чатов, передаваемый в создаваемый ViewModel.
 * @param logoutUseCase   Use-case выхода, передаваемый в создаваемый ViewModel.
 * @param navigator       Навигатор приложения.
 */
internal class ChatsViewModelFactory @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val navigator: Navigator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        ChatsViewModel(getChatsUseCase, logoutUseCase, navigator) as T
}