package com.github.sapfirmoscow.kmpchat.presentation.view.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.sapfirmoscow.kmpchat.domain.usecase.GetChatsUseCase
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

internal class ChatsViewModelFactory @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        ChatsViewModel(getChatsUseCase) as T
}