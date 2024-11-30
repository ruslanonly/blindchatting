package com.example.blindchatting.features.messenger.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.blindchatting.features.messenger.chat.ui.ChatBox
import com.example.blindchatting.features.messenger.chat.ui.add_member.AddChatMemberModal
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: Int,
    navigateBack: () -> Unit,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    var showAddChatMembersModal by remember { mutableStateOf(false) }

    val context = LocalContext.current;

    val chat by chatViewModel.chat.collectAsState()
    val chatQueryState by chatViewModel.chatQuery.collectAsState()
    val addChatMemberQueryState by chatViewModel.addChatMembersQuery.collectAsState()

    LaunchedEffect(Unit) {
        chatViewModel.getChat(chatId)
    }

    LaunchedEffect(chatQueryState) {
        when (chatQueryState) {
            is ChatQueryState.Error -> {
                val message = (chatQueryState as ChatQueryState.Error).message
                Toast.makeText(context, "Error while getting a chat: $message", Toast.LENGTH_LONG).show()
                runBlocking {
                    val result = withTimeoutOrNull(2000) {
                        repeat(10) { i ->
                            delay(200)
                        }
                    }

                    if (result == null) {
                        navigateBack()
                    } else {
                        println(result)
                    }
                }

            }
            else -> {}
        }
    }

    LaunchedEffect(addChatMemberQueryState) {
        when (addChatMemberQueryState) {
            is AddChatMemberQueryState.Success -> {
                Toast.makeText(context, "You've added new members", Toast.LENGTH_SHORT).show()
                showAddChatMembersModal = false
            }
            is AddChatMemberQueryState.Error -> {
                val message = (addChatMemberQueryState as AddChatMemberQueryState.Error).message
                Toast.makeText(context, "Error while adding new members to chat: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (chatQueryState == ChatQueryState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        val name = chat?.Name

                        Text(if (name.isNullOrEmpty()) "Chat" else name)
                    }
                },
                navigationIcon = {
                    if (chatQueryState == ChatQueryState.Success) {
                        IconButton(
                            onClick = navigateBack
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBackIosNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (chatQueryState == ChatQueryState.Success) {
                        IconButton(
                            onClick = {
                                showAddChatMembersModal = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new users",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(48.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(96.dp),
                containerColor = Color.Transparent
            ) {
                ChatBox(
                    enabled = chatQueryState == ChatQueryState.Success,
                    onMessage = { message ->
                        Log.d("Message", message)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (showAddChatMembersModal) {
                AddChatMemberModal(
                    isAdding = addChatMemberQueryState == AddChatMemberQueryState.Loading,
                    onAddChatMembers = {
                        membersIds ->  chatViewModel.addMembers(chatId, membersIds)
                    },
                    onDismissRequest = {
                        showAddChatMembersModal = false
                    }
                )
            }
        }
    }
}