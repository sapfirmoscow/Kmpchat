package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import com.github.sapfirmoscow.kmpchat.navigation.Routes
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenContent
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModel
import com.github.sapfirmoscow.kmpchat.presentation.view.chat.ChatScreen
import com.github.sapfirmoscow.kmpchat.presentation.view.chat.ChatViewModel
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsScreen
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsViewModel

/**
 * Корневой Composable приложения.
 *
 * Рендерит единый [NavDisplay] поверх бэкстека из [com.github.sapfirmoscow.kmpchat.navigation.Navigator].
 * Навигацию между экранами выполняют сами ViewModel'и — здесь колбэков навигации нет.
 *
 * @param appGraph Граф зависимостей, предоставляющий навигатор и фабрики ViewModel.
 */
@Composable
fun App(appGraph: AppGraph) {
    val navigator = appGraph.navigator

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Routes.Auth> {
                val viewModel = viewModel<AuthScreenViewModel>(
                    factory = appGraph.authScreenViewModelFactory
                )
                AuthScreenContent(viewModel)
            }
            entry<Routes.ChatsList> {
                val viewModel = viewModel<ChatsViewModel>(
                    factory = appGraph.chatsViewModelFactory
                )
                ChatsScreen(viewModel)
            }
            entry<Routes.Chat> {
                val viewModel = viewModel<ChatViewModel>(
                    factory = appGraph.chatViewModelFactory.create(it.chatId, it.chatName)
                )
                ChatScreen(viewModel)
            }
        }
    )
}
