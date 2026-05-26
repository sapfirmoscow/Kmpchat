package com.github.sapfirmoscow.kmpchat.presentation.view.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Composable-экран аутентификации.
 *
 * Отображает поля ввода логина и пароля, кнопки входа и регистрации,
 * индикатор загрузки и сообщение об ошибке. После успешной аутентификации
 * вызывает [onNavigateToChats].
 *
 * @param viewModel        ViewModel, управляющий состоянием экрана.
 * @param onNavigateToChats Колбэк навигации на экран чатов.
 */
@Composable
internal fun AuthScreenContent(
    viewModel: AuthScreenViewModel,
    onNavigateToChats: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiState
            .drop(1)
            .filter { it.isAuthenticated }
            .collect { onNavigateToChats() }
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !uiState.isLoading
                )

                if (uiState.error != null) {
                    Text(text = uiState.error ?: "", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.login(username, password) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Войти")
                        }
                        Button(
                            onClick = { viewModel.register(username, password) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Регистрация")
                        }
                    }
                }
            }
        }
    }
}
