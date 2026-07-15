package com.github.sapfirmoscow.kmpchat.presentation.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Состояние ошибки загрузки с возможностью повторить запрос.
 *
 * Общий компонент: используется на всех экранах, где неудачная загрузка
 * оставила бы пустой экран без выхода.
 *
 * @param message Текст ошибки.
 * @param onRetry Колбэк повторной загрузки.
 */
@Composable
internal fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Preview
@Composable
private fun ErrorStatePreview() {
    ErrorState(message = "Не удалось загрузить сообщения", onRetry = {})
}
