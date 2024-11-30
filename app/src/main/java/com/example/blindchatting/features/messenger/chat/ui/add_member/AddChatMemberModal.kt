package com.example.blindchatting.features.messenger.chat.ui.add_member

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blindchatting.features.messenger.chat.model.SelectableUserInfo
import com.example.blindchatting.features.messenger.contacts.all.ContactsQueryState
import com.example.blindchatting.shared.ui.Avatar
import com.example.blindchatting.shared.ui.Header
import com.example.blindchatting.shared.ui.Placeholder
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChatMemberModal(
    isAdding: Boolean,
    onAddChatMembers: (membersIds: List<Int>) -> Unit,
    onDismissRequest: () -> Unit,
    addChatMembersViewModel: AddChatMembersViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val contactList = addChatMembersViewModel.contacts.collectAsState().value
    val contactsQueryState = addChatMembersViewModel.contactsQuery.collectAsState().value

    val selectedContacts = contactList.filter {
            contact -> contact.isSelected
    }.map {
            contact -> contact.ID
    }

    fun onAddMembers() {
        onAddChatMembers(selectedContacts)
    }

    LaunchedEffect(Unit) {
        addChatMembersViewModel.getAll()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        when (contactsQueryState) {
            is ContactsQueryState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            is ContactsQueryState.Error -> {
                Placeholder(
                    icon = Icons.Outlined.Error,
                    color = MaterialTheme.colorScheme.error,
                    message = (contactsQueryState).message
                )
            }

            is ContactsQueryState.Success -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Header(text = "Add members")

                    Spacer(Modifier.size(24.dp))

                    ContactsList(
                        contacts = contactList,
                        onSelect = { contact, isSelected ->
                            addChatMembersViewModel.select(contact.ID, isSelected)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilledTonalButton(
                        enabled = !isAdding && !selectedContacts.isEmpty(),
                        onClick = { onAddMembers() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        if (isAdding) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Add")
                        }
                    }
                }

            }
            else -> {
                Placeholder(
                    icon = Icons.Outlined.PersonOutline,
                    color = MaterialTheme.colorScheme.primary,
                    message = "You've not added contacts yet. Let's start!"
                )
            }
        }
    }
}

@Composable
fun ContactsList(
    contacts: List<SelectableUserInfo>,
    onSelect: (SelectableUserInfo, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "У вас нет контактов",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(contacts, key = { contact -> contact.ID }) { contact ->
                ContactItem(
                    contact = contact,
                    onSelect = { isSelected ->
                        onSelect(contact, isSelected)
                    }
                )
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: SelectableUserInfo,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.small
            )
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(text = contact.UserName, size = 40.dp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = contact.UserName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = contact.isSelected,
            onCheckedChange = { isChecked ->
                onSelect(isChecked)
            }
        )
    }
}