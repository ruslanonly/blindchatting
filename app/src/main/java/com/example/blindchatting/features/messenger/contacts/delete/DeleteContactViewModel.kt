package com.example.blindchatting.features.messenger.contacts.delete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DeleteContactState {
    object Idle : DeleteContactState()
    object Loading : DeleteContactState()
    object Success : DeleteContactState()
    data class Error(val message: String) : DeleteContactState()
}

class DeleteContactViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _deleteContactState = MutableStateFlow<DeleteContactState>(DeleteContactState.Idle)

    val deleteContactState: StateFlow<DeleteContactState> get() = _deleteContactState

    fun deleteContact(id: Int) {
        _deleteContactState.value = DeleteContactState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.contactsService.delete(id)
                Log.d("DeleteContactViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    _deleteContactState.value = DeleteContactState.Success
                } else {
                    _deleteContactState.value =
                        DeleteContactState.Error("The contact with ID $id weren't deleted!")
                }
            } catch (e: Exception) {
                Log.e("DeleteContactViewModel", "Error during deleting contact: ${e.message}", e)
                _deleteContactState.value = DeleteContactState.Error("Error while deleting contact: ${e.message}")
            }
        }
    }
}