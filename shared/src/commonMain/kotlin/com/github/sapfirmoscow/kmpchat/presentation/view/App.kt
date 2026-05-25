package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsScreen
import com.github.sapfirmoscow.kmpchat.presentation.view.chats.ChatsViewModel

@Composable
fun App(appGraph: AppGraph) {
    val viewModel = viewModel<ChatsViewModel>(factory = appGraph.chatsViewModelFactory)
    ChatsScreen(viewModel)
}
