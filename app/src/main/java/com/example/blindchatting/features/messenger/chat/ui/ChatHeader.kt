package com.example.blindchatting.features.messenger.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blindchatting.shared.ui.Avatar

@Composable
fun ChatHeader(
    chatName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
            },
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Вернуться",
                modifier = Modifier
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            modifier = modifier
                .align(Alignment.CenterVertically),
            text = chatName.replaceFirstChar { it.uppercaseChar() },
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (true) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить чат",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .clickable(onClick = {})
                )
                if (true) {
                    Icon(
                        imageVector = Icons.Filled.GroupAdd,
                        contentDescription = "Добавить новых пользователей",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clickable(onClick = {  })
                    )
                }
            }
            Avatar(
                text = chatName,
                size = 48.dp
            )
        }
    }
}
