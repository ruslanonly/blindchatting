package com.example.blindchatting.features.messenger.chat.ui.add_member

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.features.messenger.chat.model.SelectableUserInfo
import com.example.blindchatting.features.messenger.contacts.all.ContactsQueryState
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.chats.AddChatMembersRequest
import com.example.blindchatting.shared.api.services.contacts.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun transformToSelectableContactList(contactList: List<UserInfo>): List<SelectableUserInfo> {
    return contactList.map { userInfo ->
        SelectableUserInfo(
            ID = userInfo.ID,
            Login = userInfo.Login,
            UserName = userInfo.UserName,
            isSelected = false
        )
    }
}

sealed class AddChatMembersQueryState {
    object Nothing : AddChatMembersQueryState()
    object Loading : AddChatMembersQueryState()
    object Success : AddChatMembersQueryState()
    data class Error(val message: String) : AddChatMembersQueryState()
}

class AddChatMembersViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {
    private val _contacts = MutableStateFlow<List<SelectableUserInfo>>(emptyList())
    private val _contactsQuery = MutableStateFlow<ContactsQueryState>(ContactsQueryState.Nothing)
    private val _addChatMembersQuery = MutableStateFlow<AddChatMembersQueryState>(AddChatMembersQueryState.Nothing)

    val contactsQuery: StateFlow<ContactsQueryState> get() = _contactsQuery
    val contacts: StateFlow<List<SelectableUserInfo>> get() = _contacts

    fun getAll() {
        _contactsQuery.value = ContactsQueryState.Loading

        viewModelScope.launch {
            try {
                val response = eptaChatApi.contactsService.getAll()
                Log.d("AddChatMemberViewModel", "Response received: ${response}")

                if (response.isSuccessful) {
                    response.body()?.let { contactsList ->
                        _contacts.value = transformToSelectableContactList(contactsList.toList())
                        _contactsQuery.value = ContactsQueryState.Success
                    } ?: run {
                        _contactsQuery.value = ContactsQueryState.Error("No contacts found.")
                    }
                } else {
                    _contactsQuery.value =
                        ContactsQueryState.Error("Error fetching contacts: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AddChatMemberViewModel", "Error during getting contacts: ${e.message}", e)
                _contactsQuery.value = ContactsQueryState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }

    fun select(id: Int, isSelected: Boolean) {
        val updatedContacts = _contacts.value.map { contact ->
            if (contact.ID == id) {
                contact.copy(isSelected = isSelected)
            } else {
                contact
            }
        }
        _contacts.value = updatedContacts
    }
}