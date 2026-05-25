package com.github.sapfirmoscow.kmpchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.sapfirmoscow.kmpchat.presentation.view.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appGraph = (application as KmpChatApplication).appGraph
        setContent {
            App(appGraph = appGraph)
        }
    }
}