package com.github.sapfirmoscow.kmpchat.presentation.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenContent
import com.github.sapfirmoscow.kmpchat.presentation.view.auth.AuthScreenViewModel

@Composable
fun App(appGraph: AppGraph) {
    val viewModel = viewModel<AuthScreenViewModel>(factory = appGraph.authScreenViewModelFactory)
    AuthScreenContent(viewModel)
}