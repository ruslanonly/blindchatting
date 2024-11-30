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

data class MessageInfo(
    val chatId: Int,
    val id: Int,
    val senderId: Int,
    val sendingTime: String,
    val text: String,
    val userName: String
)