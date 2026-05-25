package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.compose.runtime.Composable
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

@Composable
fun App(appGraph: AppGraph) {
    val isTokenExist = appGraph.jwtTokenRepository.getToken() != null

    val backStack =
        rememberNavBackStack(config, if (isTokenExist) Routes.ChatsList else Routes.Auth)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Routes.Auth> {
                val viewModel =
                    viewModel<AuthScreenViewModel>(factory = appGraph.authScreenViewModelFactory)
                AuthScreenContent(viewModel) { backStack.add(Routes.ChatsList) }
            }
            entry<Routes.ChatsList> {
                val viewModel =
                    viewModel<ChatsViewModel>(factory = appGraph.chatsViewModelFactory)
                ChatsScreen(viewModel)
            }

        }
    )


}
