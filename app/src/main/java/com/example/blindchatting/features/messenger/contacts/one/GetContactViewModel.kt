package com.example.blindchatting.features.messenger.contacts.one

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.contacts.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GetContactState {
    object Loading : GetContactState()
    data class Success(val contact: UserInfo) : GetContactState()
    data class Error(val message: String) : GetContactState()
}

class GetContactViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _contactState = MutableStateFlow<GetContactState>(GetContactState.Loading)
    val contactState: StateFlow<GetContactState> = _contactState

    fun getContact(id: Int) {
        viewModelScope.launch {
            _contactState.value = GetContactState.Loading
            try {
                val response = eptaChatApi.contactsService.get(id)
                Log.d("GetContactViewModel", "Response received: $response")

                if (response.isSuccessful) {
                    response.body()?.let { contact ->
                        _contactState.value = GetContactState.Success(contact)
                    } ?: run {
                        _contactState.value = GetContactState.Error("Contact with ID $id not found.")
                    }
                } else {
                    _contactState.value = GetContactState.Error("Error fetching contact: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GetContactViewModel", "Error during getting contact: ${e.message}", e)
                _contactState.value = GetContactState.Error("An error occurred: ${e.message}")
            }
        }
    }
}