package com.github.sapfirmoscow.kmpchat.presentation.view.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sapfirmoscow.kmpchat.domain.model.Message

/**
 * Composable-экран чата.
 *
 * @param viewModel ViewModel, предоставляющий UI-состояние экрана.
 */
@Composable
internal fun ChatScreen(viewModel: ChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    ChatContent(
        uiState = uiState,
        onBack = viewModel::goBack,
        onInputChange = viewModel::onInputChange,
        onSend = viewModel::sendMessage
    )
}

@Composable
internal fun ChatContent(
    uiState: ChatUiState,
    onBack: () -> Unit = {},
    onInputChange: (String) -> Unit = {},
    onSend: () -> Unit = {}
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
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(avatarColor(uiState.chatName), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.chatName.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Text(uiState.chatName)
                    }
                }
            )
        },
        bottomBar = {
            MessageInputBar(
                text = uiState.inputText,
                onValueChange = onInputChange,
                onSend = onSend
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.messages.isEmpty()) {
                Text(
                    text = "Нет сообщений",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            } else {
                MessagesList(
                    messages = uiState.messages,
                    currentUserId = uiState.currentUserId
                )
            }
        }
    }
}

@Composable
private fun MessagesList(messages: List<Message>, currentUserId: String) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            MessageBubble(message = message, isOutgoing = message.senderId == currentUserId)
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isOutgoing: Boolean) {
    val bubbleColor = if (isOutgoing) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isOutgoing) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (isOutgoing) Alignment.End else Alignment.Start
    val shape = if (isOutgoing) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(bubbleColor, shape)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp
            )
        }
        Text(
            text = formatTime(message.sentAt),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 2.dp)
        )
    }
}

@Composable
private fun MessageInputBar(
    text: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Сообщение...") },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            enabled = text.isNotBlank(),
            modifier = Modifier.align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Отправить",
                tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}

private fun avatarColor(name: String): Color {
    val key = name.take(2).lowercase()
    var hash = 0
    for (char in key) hash = char.code + (hash shl 5) - hash
    val hue = ((hash % 360) + 360) % 360
    return Color.hsl(hue.toFloat(), saturation = 0.55f, lightness = 0.70f)
}

private fun formatTime(iso: String): String {
    val t = iso.indexOf('T')
    return if (t != -1) iso.substring(t + 1, minOf(t + 6, iso.length)) else iso
}

// region Previews

private val previewMessages = listOf(
    Message(id = "1", senderId = "other", text = "Привет!", sentAt = "2024-01-01T10:00"),
    Message(id = "2", senderId = "me", text = "Привет! Как дела?", sentAt = "2024-01-01T10:01"),
    Message(id = "3", senderId = "other", text = "Отлично, спасибо! А у тебя?", sentAt = "2024-01-01T10:02"),
    Message(id = "4", senderId = "me", text = "Тоже хорошо, работаю над проектом.", sentAt = "2024-01-01T10:03"),
)

@Preview
@Composable
private fun ChatContentWithMessagesPreview() {
    ChatContent(
        uiState = ChatUiState(
            chatName = "Алексей",
            messages = previewMessages,
            currentUserId = "me",
            inputText = "Ну понятно"
        )
    )
}

@Preview
@Composable
private fun ChatContentEmptyPreview() {
    ChatContent(uiState = ChatUiState(chatName = "Мария"))
}

// endregion
