package com.github.sapfirmoscow.kmpchat

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.sapfirmoscow.kmpchat.di.AppGraph
import com.github.sapfirmoscow.kmpchat.di.DesktopAppGraph
import com.github.sapfirmoscow.kmpchat.presentation.view.App
import dev.zacsweers.metro.createGraphFactory

fun main() {
    val desktopGraph = createGraphFactory<DesktopAppGraph.Factory>().create()
    val appGraph = createGraphFactory<AppGraph.Factory>().create(desktopGraph)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Kmpchat",
        ) {
            App()
        }
    }
}

