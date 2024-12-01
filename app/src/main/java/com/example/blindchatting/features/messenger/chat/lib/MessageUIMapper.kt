package com.example.blindchatting.features.messenger.chat.lib

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.blindchatting.features.messenger.chat.model.MessageUI
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.chats.ChatConnectionMessage
import com.example.blindchatting.shared.api.services.chats.Message
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.getTimeString(): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = this.toLocalTime().format(timeFormatter)

    return time.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.getDateString(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return this.toLocalDate().format(dateFormatter)
}

class MessageUIMapper(
    private val tokenManager: TokenManager
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapMessage(message: Message): MessageUI {
        val currentUserId = tokenManager.getUserId()

        val localDateTime = OffsetDateTime.parse(message.SendingTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME).atZoneSameInstant(
            ZoneId.systemDefault()).toLocalDateTime();

        return MessageUI(
            ID = message.ID,
            Text = message.Text,
            SendingTime = message.SendingTime,
            isFromMe = message.SenderId == currentUserId,
            UserName = message.UserName,
            ChatId = message.ChatId,
            SenderId = message.SenderId,
            date = localDateTime.getDateString(),
            time = localDateTime.getTimeString()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapChatConnectionMessage(message: ChatConnectionMessage): MessageUI {
        val currentUserId = tokenManager.getUserId()

        val localDateTime = OffsetDateTime.parse(message.sendingTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME).atZoneSameInstant(
            ZoneId.systemDefault()).toLocalDateTime();

        return MessageUI(
            ID = message.id,
            Text = message.text,
            SendingTime = message.sendingTime,
            isFromMe = message.senderId == currentUserId,
            UserName = message.userName,
            ChatId = message.chatId,
            SenderId = message.senderId,
            date = localDateTime.getDateString(),
            time = localDateTime.getTimeString()
        )
    }
}