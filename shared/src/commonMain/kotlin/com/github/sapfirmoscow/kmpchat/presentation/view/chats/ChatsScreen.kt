package com.github.sapfirmoscow.kmpchat.presentation.view.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sapfirmoscow.kmpchat.presentation.view.common.ErrorState

/**
 * Composable-экран списка чатов.
 *
 * Отображает индикатор загрузки, сообщение об ошибке или список чатов
 * в зависимости от текущего [ChatsUiState].
 *
 * @param viewModel ViewModel, предоставляющий UI-состояние экрана.
 */
@Composable
internal fun ChatsScreen(viewModel: ChatsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    ChatsContent(
        uiState = uiState,
        onLogout = viewModel::logout,
        onChatClick = viewModel::openChat,
        onNewChat = viewModel::openNewChat,
        onRetry = viewModel::retry
    )
}

@Composable
internal fun ChatsContent(
    uiState: ChatsUiState,
    onLogout: () -> Unit = {},
    onChatClick: (chatId: String, chatName: String) -> Unit = { _, _ -> },
    onNewChat: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("KMPChat")
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Выйти") },
                                onClick = {
                                    menuExpanded = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewChat) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Новый чат")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState.error != null -> ErrorState(
                    message = uiState.error,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState.chats.isEmpty() -> EmptyState(
                    onNewChat = onNewChat,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> ChatsList(chats = uiState.chats, onChatClick = onChatClick)
            }
        }
    }
}

/**
 * Состояние пустого списка чатов.
 *
 * Без явной подсказки и кнопки новый пользователь видел бы пустой экран
 * и не понял, как начать переписку.
 *
 * @param onNewChat Колбэк перехода к созданию чата.
 */
@Composable
private fun EmptyState(
    onNewChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Чатов пока нет",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Найдите собеседника по имени пользователя",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Button(onClick = onNewChat) {
            Text("Начать чат")
        }
    }
}

/**
 * Отображает прокручиваемый список чатов.
 *
 * @param chats Список чатов с уже готовыми именами.
 */
@Composable
private fun ChatsList(
    chats: List<ChatListItem>,
    onChatClick: (chatId: String, chatName: String) -> Unit
) {
    LazyColumn {
        items(chats, key = { it.id }) { chat ->
            ChatItem(
                name = chat.name,
                onClick = { onChatClick(chat.id, chat.name) }
            )
        }
    }
}

/**
 * Отдельная строка чата в списке.
 *
 * Показывает аватар с первой буквой имени и название чата.
 *
 * @param name    Название чата (имена участников через запятую).
 * @param onClick Колбэк нажатия на элемент.
 */
@Composable
private fun ChatItem(
    name: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(avatarColor(name), shape = RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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

private val previewChats = listOf(
    ChatListItem(id = "1", name = "Алексей"),
    ChatListItem(id = "2", name = "Мария, Иван"),
    ChatListItem(id = "3", name = "Дмитрий Петров")
)

@Preview
@Composable
private fun ChatsContentLoadingPreview() {
    ChatsContent(uiState = ChatsUiState(isLoading = true))
}

@Preview
@Composable
private fun ChatsContentErrorPreview() {
    ChatsContent(uiState = ChatsUiState(error = "Не удалось загрузить чаты"))
}

@Preview
@Composable
private fun ChatsContentWithChatsPreview() {
    ChatsContent(uiState = ChatsUiState(chats = previewChats))
}

@Preview
@Composable
private fun ChatsContentEmptyPreview() {
    ChatsContent(uiState = ChatsUiState(chats = emptyList()))
}

@Preview
@Composable
private fun ChatItemPreview() {
    ChatItem(name = "Алексей", onClick = {})
}

// endregion