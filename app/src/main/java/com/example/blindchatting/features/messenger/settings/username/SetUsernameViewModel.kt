package com.example.blindchatting.features.messenger.settings.username

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.auth.SetUsernameRequest
import com.example.blindchatting.shared.api.services.contacts.CreateContactRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SetUsernameQueryState {
    object Nothing : SetUsernameQueryState()
    object Loading : SetUsernameQueryState()
    object Success : SetUsernameQueryState()
    data class Error(val message: String) : SetUsernameQueryState()
}

class SetUsernameViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _setUsernameQuery = MutableStateFlow<SetUsernameQueryState>(SetUsernameQueryState.Nothing)
    val setUsernameQuery: StateFlow<SetUsernameQueryState> get() = _setUsernameQuery

    fun setUsername(username: String) {
        _setUsernameQuery.value = SetUsernameQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.authService.setUsername(SetUsernameRequest(username))

                Log.d("SetUsernameViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    Log.d("SetUsernameViewModel", "Username was successfully updated!")
                    _setUsernameQuery.value = SetUsernameQueryState.Success
                } else {
                    Log.e("SetUsernameViewModel", "Failed to update username: ${response.errorBody()?.string()}")
                    _setUsernameQuery.value =
                        SetUsernameQueryState.Error("Error during creating new contact!")
                }
            } catch (e: Exception) {
                Log.e("SetUsernameViewModel", "Error during creating new contact: ${e.message}", e)
                _setUsernameQuery.value =
                    SetUsernameQueryState.Error("An error occurred: ${e.message}")
            }
        }
    }
}