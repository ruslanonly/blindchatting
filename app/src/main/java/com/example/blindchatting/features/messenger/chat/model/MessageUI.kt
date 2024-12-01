package com.example.blindchatting.features.messenger.chat.model

import com.example.blindchatting.shared.api.services.chats.Message

class MessageUI(
    val isFromMe: Boolean = false,
    val date: String,
    val time: String,
    override val ChatId: Int,
    override val ID: Int,
    override val SenderId: Int,
    override val SendingTime: String,
    override val Text: String,
    override val UserName: String
) : Message(
    ChatId,
    ID,
    SenderId,
    SendingTime,
    Text,
    UserName
)
