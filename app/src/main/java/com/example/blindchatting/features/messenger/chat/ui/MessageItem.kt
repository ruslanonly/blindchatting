package com.example.blindchatting.features.messenger.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blindchatting.features.messenger.chat.model.MessageUI
import com.example.blindchatting.shared.ui.Avatar
@Composable
fun MessageItem(message: MessageUI, modifier: Modifier = Modifier) {
    val userName = resolveUserName(message.UserName)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = resolveHorizontalArrangement(message.isFromMe),
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isFromMe) {
            Avatar(
                text = userName,
                size = 32.dp,
            )
        }

        MessageBubble(
            message = message,
            userName = userName,
            isFromMe = message.isFromMe
        )
    }
}

@Composable
private fun resolveUserName(userName: String): String =
    if (userName.isBlank()) "Нет имени" else userName

@Composable
private fun resolveHorizontalArrangement(isFromMe: Boolean): Arrangement.Horizontal =
    if (isFromMe) Arrangement.End else Arrangement.Start

@Composable
private fun MessageBubble(message: MessageUI, userName: String, isFromMe: Boolean) {
    val bubbleShape = resolveBubbleShape(isFromMe)

    Column(
        modifier = Modifier
            .padding(
                start = if (isFromMe) 0.dp else 4.dp,
                end = if (isFromMe) 4.dp else 0.dp
            )
            .clip(bubbleShape)
            .background(
                if (isFromMe) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.inversePrimary
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .widthIn(min = 70.dp, max = 280.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isFromMe) {
            DisplayUserName(userName)
        }
        DisplayMessageContent(message.Text, isFromMe)
        DisplayTimestamp(message.time)
    }
}

@Composable
private fun resolveBubbleShape(isFromMe: Boolean): RoundedCornerShape =
    if (isFromMe) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        )
    }

@Composable
private fun DisplayUserName(userName: String) {
    Text(
        text = userName,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier.padding(bottom = 1.dp),
        lineHeight = 2.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun DisplayMessageContent(messageText: String, isFromMe: Boolean) {
    Text(
        text = messageText,
        color = if (isFromMe) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 1.dp)
    )
}

@Composable
private fun DisplayTimestamp(timestamp: String) {
    Text(
        text = timestamp,
        fontSize = 12.sp,
        color = Color.Gray,
        lineHeight = 1.sp
    )
}