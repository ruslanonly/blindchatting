package com.example.blindchatting.features.messenger.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.features.messenger.chat.lib.MessageUIMapper
import com.example.blindchatting.features.messenger.chat.model.MessageUI
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.chats.AddChatMembersRequest
import com.example.blindchatting.shared.api.services.chats.Chat
import com.example.blindchatting.shared.api.services.chats.ChatConnectionMessage
import com.example.blindchatting.shared.api.services.chats.MakeMessage
import com.example.blindchatting.shared.api.services.chats.Message
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

const val PAGE_SIZE = 100;

sealed class ChatQueryState {
    object Nothing : ChatQueryState()
    object Loading : ChatQueryState()
    object Success : ChatQueryState()
    data class Error(val message: String) : ChatQueryState()
}

sealed class AddChatMemberQueryState {
    object Nothing : AddChatMemberQueryState()
    object Loading : AddChatMemberQueryState()
    object Success : AddChatMemberQueryState()
    data class Error(val message: String) : AddChatMemberQueryState()
}

class ChatViewModel(
    private val eptaChatApi: EptaChatApi,
    private val messageUIMapper: MessageUIMapper
) : ViewModel() {
    private var webSocket: WebSocket? = null

    private val _currentPageId = MutableStateFlow<Int>(1)
    private val _messages = MutableStateFlow<List<MessageUI>>(emptyList())

    private val _chat = MutableStateFlow<Chat?>(null)
    private val _chatQuery = MutableStateFlow<ChatQueryState>(ChatQueryState.Nothing)
    private val _addChatMembersQuery = MutableStateFlow<AddChatMemberQueryState>(AddChatMemberQueryState.Nothing)

    val addChatMembersQuery: StateFlow<AddChatMemberQueryState> get() = _addChatMembersQuery
    val chatQuery: StateFlow<ChatQueryState> get() = _chatQuery
    val chat: StateFlow<Chat?> get() = _chat

    val messages: StateFlow<List<MessageUI>> get() = _messages

    private val _chatIsReady = MutableStateFlow<Boolean>(false)
    val chatIsReady: StateFlow<Boolean> get() = _chatIsReady

    private fun connectToChat(chatId: Int) {
        webSocket = eptaChatApi.chatService.connect(chatId, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("ChatViewModel", "Получилось подключиться к чату $chatId")

                _chatIsReady.value = true;
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val incomingMessage = Gson().fromJson(text, ChatConnectionMessage::class.java)
                    val message = listOf(messageUIMapper.mapChatConnectionMessage(incomingMessage))
                    _messages.value = message + _messages.value
                    Log.d("ChatViewModel", "Получено сообщение ${incomingMessage.userName} ${incomingMessage.text}")
                } catch (e: Exception) {
                    Log.d("ChatViewModel", "Получено сообщение $text, но его не получилось обработать ${e.toString()}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("ChatViewModel", "Ошибка подключения к WS: $webSocket ${t.message} ${response.toString()}")
                _chatIsReady.value = false;
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMessages(chatId: Int, pageId: Int) {
        viewModelScope.launch {
            try {
                _currentPageId.value = pageId;
                val response = eptaChatApi.chatsService.getChatMessages(chatId, pageId)
                Log.d("ChatViewModel", "Сделан запрос loadMessages($chatId, $pageId): ${response}")

                if (response.isSuccessful) {
                    val newMessages = response.body() ?: emptyList()
                    val rawMessages = if (pageId == 0) {
                        newMessages.reversed()
                    } else {
                        _messages.value + newMessages.reversed()
                    }

                    _messages.value = rawMessages.map { message -> messageUIMapper.mapMessage(message)}
                } else {
                    Log.d("ChatViewModel", "Возникла ошибка при загрузке: ${response}")
                }
            } catch (e: Exception) {
                Log.d("ChatViewModel", "Возникла ошибка при загрузке: ${e.message}")
            }
        }
    }

    private fun getChat(id: Int) {
        _chatQuery.value = ChatQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.chatsService.getChat(id)
                Log.d("ChatViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    response.body()?.let { chat ->
                        _chat.value = chat
                        _chatQuery.value = ChatQueryState.Success
                    } ?: run {
                        _chatQuery.value = ChatQueryState.Error("Chat weren't found.")
                    }
                } else {
                    _chatQuery.value =
                        ChatQueryState.Error("Error fetching chat: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error during getting chat: ${e.message}", e)
                _chatQuery.value = ChatQueryState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initChat(chatId: Int) {
        getChat(chatId)
        loadMessages(chatId, 0)
        connectToChat(chatId)
    }

    fun sendMessage(text: String) {
        webSocket?.let { socket ->
            val message = MakeMessage(text)
            val json = Gson().toJson(message)
            eptaChatApi.chatService.sendMessage(socket, json)
            Log.d("ChatViewModel", "Отправлено сообщение $text")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadOlderMessages(chatId: Int) {
        val nextPageId = (_messages.value.size / PAGE_SIZE) + 1

        if (nextPageId > _currentPageId.value) {
            loadMessages(chatId, nextPageId)
        }
    }

    fun addMembers(chatId: Int, members: List<Int>) {
        _addChatMembersQuery.value = AddChatMemberQueryState.Nothing
        viewModelScope.launch {
            _addChatMembersQuery.value = AddChatMemberQueryState.Loading
            try {
                val response = eptaChatApi.chatsService.addChatMembers(AddChatMembersRequest(
                    chat_id = chatId,
                    members_ids = members,
                ))
                Log.d("AddChatMemberViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    response.body()?.let { contactsList ->
                        _addChatMembersQuery.value = AddChatMemberQueryState.Success
                    } ?: run {
                        _addChatMembersQuery.value = AddChatMemberQueryState.Error("Didn't add selected users.")
                    }
                } else {
                    _addChatMembersQuery.value =
                        AddChatMemberQueryState.Error("Error fetching contacts: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AddChatMemberViewModel", "Error during getting contacts: ${e.message}", e)
                _addChatMembersQuery.value =
                    AddChatMemberQueryState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.let { socket ->
            eptaChatApi.chatService.disconnect(socket)
        }
    }
}