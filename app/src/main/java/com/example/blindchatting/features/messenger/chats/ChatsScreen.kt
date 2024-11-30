package com.example.blindchatting.features.messenger.contacts

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blindchatting.features.messenger.chats.all.ChatsQueryState
import com.example.blindchatting.features.messenger.chats.all.ChatsViewModel
import com.example.blindchatting.features.messenger.chats.create.CreateChatModal
import com.example.blindchatting.features.messenger.chats.create.CreateChatQueryState
import com.example.blindchatting.features.messenger.chats.create.CreateChatViewModel
import com.example.blindchatting.shared.api.services.chats.Chat
import com.example.blindchatting.shared.routing.Route
import com.example.blindchatting.shared.ui.Avatar
import com.example.blindchatting.shared.ui.Placeholder
import com.example.blindchatting.shared.ui.layout.NavBar
import com.example.blindchatting.shared.ui.layout.NavBarItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatsScreen(
    navController: NavController,
    chatsViewModel: ChatsViewModel = koinViewModel(),
    createChatViewModel: CreateChatViewModel = koinViewModel(),
) {
    var showCreateChatModal by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatsViewModel.getAll()
    }

    val createChatState by createChatViewModel.createChatQuery.collectAsState()
    val chats by chatsViewModel.chats.collectAsState()
    val chatsQueryState by chatsViewModel.chatsQuery.collectAsState()

    LaunchedEffect(createChatState) {
        when (createChatState) {
            is CreateChatQueryState.Success -> {
                Toast.makeText(context, "You've created new contact", Toast.LENGTH_SHORT).show()
                showCreateChatModal = false
                chatsViewModel.getAll()
            }
            is CreateChatQueryState.Error -> {
                val message = (createChatState as CreateChatQueryState.Error).message
                Toast.makeText(context, "Error while creating contact: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        bottomBar = {
            NavBar(
                navController = navController,
                activeItem = NavBarItem.Chats
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateChatModal = true }) {
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
            when (chatsQueryState) {
                ChatsQueryState.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                is ChatsQueryState.Error -> {
                    Placeholder(
                        icon = Icons.Outlined.Error,
                        color = MaterialTheme.colorScheme.error,
                        message = (chatsQueryState as ChatsQueryState.Error).message
                    )
                }
                ChatsQueryState.Success -> {
                    if (chats.isEmpty()) {
                        Placeholder(
                            icon = Icons.Outlined.PersonOutline,
                            color = MaterialTheme.colorScheme.primary,
                            message = "You've not added chats yet. Let's start!"
                        )
                    } else {
                        LazyColumn {
                            items(chats) { chat ->
                                Chat(
                                    chat,
                                    onGoToChat = {
                                        navController.navigate(Route.Messenger.Chat(chat.id))
                                    }
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
                        message = "You've not added chats yet. Let's start!"
                    )
                }
            }

            if (showCreateChatModal) {
                CreateChatModal (
                    onCreateChat = { chatName ->
                        createChatViewModel.createChat(chatName, emptyList(), isDirect = false)
                    },
                    isLoading = createChatState == CreateChatQueryState.Loading,
                    onDismissRequest = {
                        showCreateChatModal = false
                    }
                )
            }
        }
    }
}

@Composable
fun Chat(
    chat: Chat,
    onGoToChat: () -> Unit
) {
    ListItem(
        headlineContent = { Text(chat.Name) },
        supportingContent = {
            if (chat.IsDirect) {
                Text("Direct chat", color = MaterialTheme.colorScheme.secondary)
            } else {
                Text("Group chat", color = MaterialTheme.colorScheme.secondary)
            }
        },
        leadingContent = {
            Avatar(text = chat.Name, size = 54.dp)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
                )
        },
        modifier = Modifier.clickable(onClick = onGoToChat)
    )
}