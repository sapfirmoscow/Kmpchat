package com.github.sapfirmoscow.kmpchat.presentation.view.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun AuthScreenContent(viewModel: AuthScreenViewModel) {


    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    Scaffold() { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize() ,horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                TextField(
                    value = loginText,
                    onValueChange = {loginText = it},
                    label = { Text("Username") }
                )
                TextField(
                    value = passwordText,
                    onValueChange = { passwordText = it},
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.register(loginText, passwordText) }) {
                    Text("Register/login")
                }
            }

        }
    }
}

