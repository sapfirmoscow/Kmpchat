package com.github.sapfirmoscow.kmpchat.presentation.view.newchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sapfirmoscow.kmpchat.domain.model.FoundUser

/**
 * Composable-экран создания нового чата.
 *
 * @param viewModel ViewModel, предоставляющий UI-состояние экрана.
 */
@Composable
internal fun NewChatScreen(viewModel: NewChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    NewChatContent(
        uiState = uiState,
        onBack = viewModel::goBack,
        onUsernameChange = viewModel::onUsernameChange,
        onSearch = viewModel::search,
        onStartChat = viewModel::startChat
    )
}

@Composable
internal fun NewChatContent(
    uiState: NewChatUiState,
    onBack: () -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onStartChat: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = { Text("Новый чат") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Имя пользователя") },
                supportingText = { Text("Точное имя собеседника") },
                singleLine = true,
                enabled = !uiState.isCreatingChat,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() })
            )

            Button(
                onClick = onSearch,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.username.isNotBlank() &&
                    !uiState.isSearching &&
                    !uiState.isCreatingChat
            ) {
                Text("Найти")
            }

            when {
                uiState.isSearching -> Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                uiState.error != null -> Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )

                uiState.notFound -> Text(
                    text = "Пользователь не найден",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                uiState.foundUser != null -> FoundUserCard(
                    user = uiState.foundUser,
                    isCreatingChat = uiState.isCreatingChat,
                    onStartChat = onStartChat
                )
            }
        }
    }
}

/**
 * Карточка найденного пользователя с подтверждением создания чата.
 *
 * @param user           Найденный пользователь.
 * @param isCreatingChat `true`, пока создаётся чат.
 * @param onStartChat    Колбэк подтверждения.
 */
@Composable
private fun FoundUserCard(
    user: FoundUser,
    isCreatingChat: Boolean,
    onStartChat: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(avatarColor(user.username), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.username.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                text = user.username,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )

            Button(onClick = onStartChat, enabled = !isCreatingChat) {
                if (isCreatingChat) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Написать")
                }
            }
        }
    }
}

private fun avatarColor(name: String): Color {
    val key = name.take(2).lowercase()
    var hash = 0
    for (char in key) {
        hash = char.code + (hash shl 5) - hash
    }
    val hue = ((hash % 360) + 360) % 360
    return Color.hsl(hue.toFloat(), saturation = 0.55f, lightness = 0.70f)
}

// region Previews

@Preview
@Composable
private fun NewChatEmptyPreview() {
    NewChatContent(uiState = NewChatUiState())
}

@Preview
@Composable
private fun NewChatSearchingPreview() {
    NewChatContent(uiState = NewChatUiState(username = "alice", isSearching = true))
}

@Preview
@Composable
private fun NewChatFoundPreview() {
    NewChatContent(
        uiState = NewChatUiState(
            username = "alice",
            foundUser = FoundUser(id = "u1", username = "alice")
        )
    )
}

@Preview
@Composable
private fun NewChatNotFoundPreview() {
    NewChatContent(uiState = NewChatUiState(username = "nobody", notFound = true))
}

@Preview
@Composable
private fun NewChatErrorPreview() {
    NewChatContent(
        uiState = NewChatUiState(
            username = "alice",
            error = "Слишком много запросов. Попробуйте через минуту"
        )
    )
}

// endregion
