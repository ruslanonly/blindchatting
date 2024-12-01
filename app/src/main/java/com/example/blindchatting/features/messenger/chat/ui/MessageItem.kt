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
    val userName = if (message.UserName.isBlank()) "Нет имени" else message.UserName

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isFromMe) {
            Avatar(
                text = userName,
                size = 32.dp,
            )
        }

        val bubbleShape = if (message.isFromMe) {
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

        Column(
            modifier = Modifier
                .padding(
                    start = if (message.isFromMe) 0.dp else 4.dp,
                    end = if (message.isFromMe) 4.dp else 0.dp
                )
                .clip(bubbleShape)
                .background(
                    if (message.isFromMe) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(min = 70.dp, max = 280.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (!message.isFromMe) {
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(bottom = 1.dp),
                    lineHeight = 2.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = message.Text,
                color = if (message.isFromMe) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Text(
                text = message.time,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End),
                lineHeight = 1.sp
            )
        }
    }
}
