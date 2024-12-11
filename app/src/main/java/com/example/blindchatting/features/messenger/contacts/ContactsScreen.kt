package com.example.blindchatting.features.messenger.contacts

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blindchatting.features.messenger.chats.create.CreateChatQueryState
import com.example.blindchatting.features.messenger.chats.create.CreateChatViewModel
import com.example.blindchatting.features.messenger.contacts.all.ContactsQueryState
import com.example.blindchatting.features.messenger.contacts.all.ContactsViewModel
import com.example.blindchatting.features.messenger.contacts.create.CreateContactModal
import com.example.blindchatting.features.messenger.contacts.create.CreateContactQueryState
import com.example.blindchatting.features.messenger.contacts.create.CreateContactViewModel
import com.example.blindchatting.features.messenger.contacts.delete.DeleteContactState
import com.example.blindchatting.features.messenger.contacts.delete.DeleteContactViewModel
import com.example.blindchatting.shared.api.services.contacts.UserInfo
import com.example.blindchatting.shared.ui.Avatar
import com.example.blindchatting.shared.ui.Placeholder
import com.example.blindchatting.shared.ui.layout.NavBar
import com.example.blindchatting.shared.ui.layout.NavBarItem
import org.koin.androidx.compose.koinViewModel
@Composable
fun ContactsScreen(
    navController: NavController,
    contactsViewModel: ContactsViewModel = koinViewModel(),
    createContactViewModel: CreateContactViewModel = koinViewModel(),
    createChatViewModel: CreateChatViewModel = koinViewModel(),
    deleteContactViewModel: DeleteContactViewModel = koinViewModel()
) {
    var showCreateContactModal by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Observing ViewModel states
    val createContactState by createContactViewModel.createContactQuery.collectAsState()
    val createChatState by createChatViewModel.createChatQuery.collectAsState()
    val contacts by contactsViewModel.contacts.collectAsState()
    val contactsQueryState by contactsViewModel.contactsQuery.collectAsState()
    val deletingContacts by deleteContactViewModel.deletingContacts.collectAsState()
    val deleteContactState by deleteContactViewModel.deleteContactState.collectAsState()

    // Effects to handle state changes
    LaunchedEffect(Unit) { contactsViewModel.getAll() }
    HandleDeleteContactEffect(deleteContactState, contactsViewModel, context)
    HandleCreateChatEffect(createChatState, context)
    HandleCreateContactEffect(createContactState, contactsViewModel, context) { showCreateContactModal = false }

    Scaffold(
        bottomBar = { NavBar(navController = navController, activeItem = NavBarItem.Contacts) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateContactModal = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ContactListContent(
                contactsQueryState = contactsQueryState,
                contacts = contacts,
                createChatState = createChatState,
                deletingContacts = deletingContacts,
                deleteContactViewModel = deleteContactViewModel,
                createChatViewModel = createChatViewModel
            )

            if (showCreateContactModal) {
                CreateContactModal(
                    isLoading = createContactState == CreateContactQueryState.Loading,
                    onCreateContact = { contactLogin -> createContactViewModel.createContact(contactLogin) },
                    onDismissRequest = { showCreateContactModal = false }
                )
            }
        }
    }
}

@Composable
private fun HandleDeleteContactEffect(
    deleteContactState: DeleteContactState,
    contactsViewModel: ContactsViewModel,
    context: Context
) {
    LaunchedEffect(deleteContactState) {
        when (deleteContactState) {
            is DeleteContactState.Success -> {
                Toast.makeText(context, "You've deleted a contact", Toast.LENGTH_SHORT).show()
                contactsViewModel.getAll()
            }
            is DeleteContactState.Error -> {
                val message = deleteContactState.message
                Toast.makeText(context, "Error while deleting a contact: $message", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}

@Composable
private fun HandleCreateChatEffect(
    createChatState: CreateChatQueryState,
    context: Context
) {
    LaunchedEffect(createChatState) {
        when (createChatState) {
            is CreateChatQueryState.Success -> {
                Toast.makeText(context, "You've created a chat", Toast.LENGTH_SHORT).show()
            }
            is CreateChatQueryState.Error -> {
                val message = createChatState.message
                Toast.makeText(context, "Error while creating a chat: $message", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}

@Composable
private fun HandleCreateContactEffect(
    createContactState: CreateContactQueryState,
    contactsViewModel: ContactsViewModel,
    context: Context,
    onModalDismiss: () -> Unit
) {
    LaunchedEffect(createContactState) {
        when (createContactState) {
            is CreateContactQueryState.Success -> {
                Toast.makeText(context, "You've created new contact", Toast.LENGTH_SHORT).show()
                onModalDismiss()
                contactsViewModel.getAll()
            }
            is CreateContactQueryState.Error -> {
                val message = createContactState.message
                Toast.makeText(context, "Error while creating contact: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}

@Composable
private fun ContactListContent(
    contactsQueryState: ContactsQueryState,
    contacts: List<UserInfo>,
    createChatState: CreateChatQueryState,
    deletingContacts: List<Int>,
    deleteContactViewModel: DeleteContactViewModel,
    createChatViewModel: CreateChatViewModel
) {
    when (contactsQueryState) {
        ContactsQueryState.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        is ContactsQueryState.Error -> {
            Placeholder(
                icon = Icons.Outlined.Error,
                color = MaterialTheme.colorScheme.error,
                message = contactsQueryState.message
            )
        }
        ContactsQueryState.Success -> {
            if (contacts.isEmpty()) {
                Placeholder(
                    icon = Icons.Outlined.PersonOutline,
                    color = MaterialTheme.colorScheme.primary,
                    message = "You've not added contacts yet. Let's start!"
                )
            } else {
                LazyColumn {
                    items(contacts) { contact ->
                        Contact(
                            contact = contact,
                            onDelete = { deleteContactViewModel.deleteContact(contact.ID) },
                            onCreateChat = {
                                createChatViewModel.createChat(
                                    chatName = "Chat with ${contact.UserName}",
                                    isDirect = true,
                                    members = listOf(contact.ID)
                                )
                            },
                            isCreatingChat = createChatState == CreateChatQueryState.Loading,
                            isDeleting = deletingContacts.contains(contact.ID),
                        )
                        HorizontalDivider()
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

@Composable
fun Contact(
    isCreatingChat: Boolean,
    isDeleting: Boolean,
    contact: UserInfo,
    onCreateChat: () -> Unit,
    onDelete: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(contact.Login) },
        supportingContent = { Text(contact.UserName) },
        leadingContent = { Avatar(text = contact.Login, size = 54.dp) },
        trailingContent = {
            Row {
                IconButton(
                    onClick = onCreateChat,
                    enabled = !isCreatingChat
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = if (isCreatingChat) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    )
}