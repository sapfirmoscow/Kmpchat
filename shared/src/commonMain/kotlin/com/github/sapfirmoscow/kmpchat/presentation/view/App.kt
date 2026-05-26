package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import com.github.sapfirmoscow.kmpchat.navigation.Routes
import com.github.sapfirmoscow.kmpchat.navigation.config
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenContent
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModel
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsScreen
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsViewModel

/**
 * Корневой Composable приложения.
 *
 * Наблюдает за [AuthState] и отображает соответствующий экран:
 * - [AuthState.Loading] — индикатор загрузки;
 * - [AuthState.Unauthenticated] — стек навигации с экраном авторизации;
 * - [AuthState.Authenticated] — стек навигации со списком чатов.
 *
 * @param appGraph Граф зависимостей, предоставляющий фабрики ViewModel.
 */
@Composable
fun App(appGraph: AppGraph) {
    val appViewModel = viewModel<AppViewModel>(factory = appGraph.appViewModelFactory)
    val authState by appViewModel.authState.collectAsState()

    when (authState) {
        AuthState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        AuthState.Unauthenticated -> {
            val backStack = rememberNavBackStack(config, Routes.Auth)
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<Routes.Auth> {
                        val viewModel = viewModel<AuthScreenViewModel>(
                            factory = appGraph.authScreenViewModelFactory
                        )
                        AuthScreenContent(viewModel) { appViewModel.onLoginSuccess() }
                    }
                }
            )
        }
        AuthState.Authenticated -> {
            val backStack = rememberNavBackStack(config, Routes.ChatsList)
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<Routes.ChatsList> {
                        val viewModel = viewModel<ChatsViewModel>(
                            factory = appGraph.chatsViewModelFactory
                        )
                        ChatsScreen(viewModel)
                    }
                }
            )
        }
    }
}
