package com.example.blindchatting.features.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.chats.AddChatMembersRequest
import com.example.blindchatting.shared.api.services.chats.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _chat = MutableStateFlow<Chat?>(null)
    private val _chatQuery = MutableStateFlow<ChatQueryState>(ChatQueryState.Nothing)
    private val _addChatMembersQuery = MutableStateFlow<AddChatMemberQueryState>(AddChatMemberQueryState.Nothing)

    val addChatMembersQuery: StateFlow<AddChatMemberQueryState> get() = _addChatMembersQuery
    val chatQuery: StateFlow<ChatQueryState> get() = _chatQuery
    val chat: StateFlow<Chat?> get() = _chat

    fun getChat(id: Int) {
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
}