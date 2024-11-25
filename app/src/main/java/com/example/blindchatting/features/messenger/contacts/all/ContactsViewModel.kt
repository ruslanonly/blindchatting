package com.example.blindchatting.features.messenger.contacts.all

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.contacts.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ContactsQueryState {
    object Nothing : ContactsQueryState()
    object Loading : ContactsQueryState()
    object Success : ContactsQueryState()
    data class Error(val message: String) : ContactsQueryState()
}

class ContactsViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _contacts = MutableStateFlow<List<UserInfo>>(emptyList())
    private val _contactsQuery = MutableStateFlow<ContactsQueryState>(ContactsQueryState.Nothing)
    val contactsQuery: StateFlow<ContactsQueryState> get() = _contactsQuery
    val contacts: StateFlow<List<UserInfo>> get() = _contacts

    fun getAll() {
        _contactsQuery.value = ContactsQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.contactsService.getAll()
                Log.d("ContactsViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    response.body()?.let { contactsList ->
                        _contacts.value = contactsList.toList()
                        _contactsQuery.value = ContactsQueryState.Success
                    } ?: run {
                        _contactsQuery.value = ContactsQueryState.Error("No contacts found.")
                    }
                } else {
                    _contactsQuery.value =
                        ContactsQueryState.Error("Error fetching contacts: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ContactsViewModel", "Error during getting contacts: ${e.message}", e)
                _contactsQuery.value = ContactsQueryState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }
}