package com.example.blindchatting.features.messenger.chats.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.features.messenger.contacts.delete.DeleteContactState
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.chats.CreateChatRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreateChatQueryState {
    object Nothing : CreateChatQueryState()
    object Loading : CreateChatQueryState()
    object Success : CreateChatQueryState()
    data class Error(val message: String) : CreateChatQueryState()
}

class CreateChatViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _createChatQuery = MutableStateFlow<CreateChatQueryState>(
        CreateChatQueryState.Nothing)

    val createChatQuery: StateFlow<CreateChatQueryState> get() = _createChatQuery

    fun createChat(
        chatName: String,
        members: List<Int>,
        isDirect: Boolean
    ) {
        _createChatQuery.value = CreateChatQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.chatsService.create(CreateChatRequest(
                    name = chatName,
                    members_ids = members,
                    is_direct = isDirect
                ))

                Log.d("CreateChatViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    Log.d("CreateChatViewModel", "Chat has been successfully created!")
                    _createChatQuery.value = CreateChatQueryState.Success
                } else {
                    Log.e("CreateChatViewModel", "Failed to create chat: ${response.errorBody()?.string()}")
                    _createChatQuery.value =
                        CreateChatQueryState.Error("Chat weren't found!")
                }
            } catch (e: Exception) {
                Log.e("CreateChatViewModel", "Error during creating new chat: ${e.message}", e)
                _createChatQuery.value =
                    CreateChatQueryState.Error("An error occurred: ${e.message}")
            }
        }
    }
}