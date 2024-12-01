package com.example.blindchatting.shared.api.services.chats

data class CreateChatRequest(
    val is_direct: Boolean,
    val members_ids: List<Int>,
    val name: String
)

data class CreateChatResponse(
   val chat_id: Int
)

data class AddChatMembersRequest(
    val chat_id: Int,
    val members_ids: List<Int>
)

data class ChatOwner(
    val id: Int,
    val Login: String,
    val Password: String,
    val UserName: String
)

data class Chat(
    val id: Int,
    val IsDirect: Boolean,
    val Name: String,
    val Owner: ChatOwner,
    val OwnerId: Int
)

open class Message(
    open val ChatId: Int,
    open val ID: Int,
    open val SenderId: Int,
    open val SendingTime: String,
    open val Text: String,
    open val UserName: String
)

open class ChatConnectionMessage(
    open val chatId: Int,
    open val id: Int,
    open val senderId: Int,
    open val sendingTime: String,
    open val text: String,
    open val userName: String
)

data class MakeMessage(
    val text: String,
)