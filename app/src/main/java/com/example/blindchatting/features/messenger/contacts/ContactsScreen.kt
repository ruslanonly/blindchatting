package com.example.blindchatting.features.messenger.contacts

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
import com.example.blindchatting.features.messenger.contacts.all.ContactsQueryState
import com.example.blindchatting.features.messenger.contacts.all.ContactsViewModel
import com.example.blindchatting.features.messenger.contacts.all.components.ContactItem
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
) {
    var showCreateContactModal by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        contactsViewModel.getAll()
    }

    val createContactState by createContactViewModel.createContactQuery.collectAsState()
    val contacts by contactsViewModel.contacts.collectAsState()
    val contactsQueryState by contactsViewModel.contactsQuery.collectAsState()

    LaunchedEffect(createContactState) {
        when (createContactState) {
            is CreateContactQueryState.Success -> {
                Toast.makeText(context, "You've created new contact", Toast.LENGTH_SHORT).show()
                showCreateContactModal = false
                contactsViewModel.getAll()
            }
            is CreateContactQueryState.Error -> {
                val message = (createContactState as CreateContactQueryState.Error).message
                Toast.makeText(context, "Error while creating contact: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        bottomBar = {
            NavBar(
                navController = navController,
                activeItem = NavBarItem.Contacts
            )
        },
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
                .padding(16.dp),
        ) {
            when (contactsQueryState) {
                ContactsQueryState.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                is ContactsQueryState.Error -> {
                    Placeholder(
                        icon = Icons.Outlined.Error,
                        color = MaterialTheme.colorScheme.error,
                        message = (contactsQueryState as ContactsQueryState.Error).message
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
                                    onDelete = { contactsViewModel.getAll() },
                                    onCreateChat = {},
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

            if (showCreateContactModal) {
                CreateContactModal(
                    isLoading = createContactState == CreateContactQueryState.Loading,
                    onCreateContact = { contactLogin ->
                        createContactViewModel.createContact(contactLogin)
                    },
                    onDismissRequest = {
                        showCreateContactModal = false
                    }
                )
            }
        }
    }
}

@Composable
fun Contact(
    contact: UserInfo,
    onCreateChat: () -> Unit,
    onDelete: () -> Unit,
    deleteContactViewModel: DeleteContactViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val deleteContactState by deleteContactViewModel.deleteContactState.collectAsState()

    LaunchedEffect(deleteContactState) {
        when (deleteContactState) {
            is DeleteContactState.Success -> {
                Toast.makeText(context, "You've deleted a contact", Toast.LENGTH_SHORT).show()
                Log.d("Hello", deleteContactState.toString())
                onDelete()
            }
            is DeleteContactState.Error -> {
                val message = (deleteContactState as DeleteContactState.Error).message
                Toast.makeText(context, "Error while deleting a contact: $message", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    ListItem(
        headlineContent = { Text(contact.Login) },
        supportingContent = { Text(contact.UserName) },
        leadingContent = { Avatar(text = contact.Login, size = 54.dp) },
        trailingContent = {
            Row {
                IconButton(onClick = { onCreateChat() }) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {
                    deleteContactViewModel.deleteContact(contact.ID)
                }) {
                    if (deleteContactState == DeleteContactState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
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