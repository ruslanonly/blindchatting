package com.example.blindchatting.features.messenger.chats.all

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.chats.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ChatsQueryState {
    object Nothing : ChatsQueryState()
    object Loading : ChatsQueryState()
    object Success : ChatsQueryState()
    data class Error(val message: String) : ChatsQueryState()
}

class ChatsViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    private val _chatsQuery = MutableStateFlow<ChatsQueryState>(ChatsQueryState.Nothing)
    val chatsQuery: StateFlow<ChatsQueryState> get() = _chatsQuery
    val chats: StateFlow<List<Chat>> get() = _chats

    fun getAll() {
        _chatsQuery.value = ChatsQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.chatsService.getAll()
                Log.d("ChatsViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    response.body()?.let { chatsList ->
                        _chats.value = chatsList.toList()
                        _chatsQuery.value = ChatsQueryState.Success
                    } ?: run {
                        _chatsQuery.value = ChatsQueryState.Error("No chats found.")
                    }
                } else {
                    _chatsQuery.value =
                        ChatsQueryState.Error("Error fetching chats: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ChatsViewModel", "Error during getting chats: ${e.message}", e)
                _chatsQuery.value = ChatsQueryState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }
}