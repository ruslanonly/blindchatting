package com.example.blindchatting.features.messenger.contacts.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.contacts.CreateContactRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreateContactQueryState {
    object Nothing : CreateContactQueryState()
    object Loading : CreateContactQueryState()
    object Success : CreateContactQueryState()
    data class Error(val message: String) : CreateContactQueryState()
}

class CreateContactViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _createContactQuery = MutableStateFlow<CreateContactQueryState>(CreateContactQueryState.Nothing)
    val createContactQuery: StateFlow<CreateContactQueryState> get() = _createContactQuery

    fun createContact(contactLogin: String) {
        _createContactQuery.value = CreateContactQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.contactsService.createOne(CreateContactRequest(contactLogin))

                Log.d("CreateContactViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    Log.d("CreateContactViewModel", "Contact has been successfully created!")
                    _createContactQuery.value = CreateContactQueryState.Success
                } else {
                    Log.e("CreateContactViewModel", "Failed to create contact: ${response.errorBody()?.string()}")
                    _createContactQuery.value =
                        CreateContactQueryState.Error("Contact weren't found!")
                }
            } catch (e: Exception) {
                Log.e("CreateContactViewModel", "Error during creating new contact: ${e.message}", e)
                _createContactQuery.value =
                    CreateContactQueryState.Error("An error occurred: ${e.message}")
            }
        }
    }
}