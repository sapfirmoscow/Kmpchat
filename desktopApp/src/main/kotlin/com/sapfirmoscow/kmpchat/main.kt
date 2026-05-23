package com.sapfirmoscow.kmpchat

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sapfirmoscow.kmpchat.presentation.view.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kmpchat",
    ) {
        App()
    }
}