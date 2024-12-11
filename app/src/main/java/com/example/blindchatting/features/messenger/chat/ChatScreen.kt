package com.example.blindchatting.features.messenger.chat

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.blindchatting.features.messenger.chat.model.MessageUI
import com.example.blindchatting.features.messenger.chat.ui.ChatBox
import com.example.blindchatting.features.messenger.chat.ui.MessageItem
import com.example.blindchatting.features.messenger.chat.ui.add_member.AddChatMemberModal
import com.example.blindchatting.shared.api.services.chats.Chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    chatId: Int,
    navigateBack: () -> Unit,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    var showAddChatMembersModal by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val messages by chatViewModel.messages.collectAsState()
    val chat by chatViewModel.chat.collectAsState()
    val chatQueryState by chatViewModel.chatQuery.collectAsState()
    val addChatMemberQueryState by chatViewModel.addChatMembersQuery.collectAsState()
    val chatIsReady by chatViewModel.chatIsReady.collectAsState()

    HandleChatInitialization(chatViewModel, chatId)
    HandleChatQueryState(chatQueryState, context, navigateBack)
    HandleAddChatMemberQueryState(addChatMemberQueryState, context) {
        showAddChatMembersModal = false
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                chatQueryState,
                chat,
                navigateBack,
                onAddChatMembers = {
                    showAddChatMembersModal = true
                })
             },
        bottomBar = {
            ChatBottomBar(
                isEnabled = chatQueryState == ChatQueryState.Success && chatIsReady,
                onMessage = { chatViewModel.sendMessage(it) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        ChatContent(
            messages = messages,
            chatId = chatId,
            chatViewModel = chatViewModel,
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        )

        if (showAddChatMembersModal) {
            AddChatMemberModal(
                isAdding = addChatMemberQueryState == AddChatMemberQueryState.Loading,
                onAddChatMembers = { membersIds -> chatViewModel.addMembers(chatId, membersIds) },
                onDismissRequest = { showAddChatMembersModal = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HandleChatInitialization(chatViewModel: ChatViewModel, chatId: Int) {
    LaunchedEffect(Unit) {
        chatViewModel.initChat(chatId)
        Log.d("ChatScreen", "Init chat: $chatId")
    }
}

@Composable
private fun HandleChatQueryState(
    chatQueryState: ChatQueryState,
    context: Context,
    navigateBack: () -> Unit
) {
    LaunchedEffect(chatQueryState) {
        if (chatQueryState is ChatQueryState.Error) {
            val message = chatQueryState.message
            Toast.makeText(context, "Error while getting a chat: $message", Toast.LENGTH_LONG).show()
            runBlocking {
                val result = withTimeoutOrNull(2000) { repeat(10) { delay(200) } }
                if (result == null) navigateBack()
            }
        }
    }
}

@Composable
private fun HandleAddChatMemberQueryState(
    addChatMemberQueryState: AddChatMemberQueryState,
    context: Context,
    onDismiss: () -> Unit
) {
    LaunchedEffect(addChatMemberQueryState) {
        when (addChatMemberQueryState) {
            is AddChatMemberQueryState.Success -> {
                Toast.makeText(context, "You've added new members", Toast.LENGTH_SHORT).show()
                onDismiss()
            }
            is AddChatMemberQueryState.Error -> {
                val message = addChatMemberQueryState.message
                Toast.makeText(context, "Error while adding new members to chat: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    chatQueryState: ChatQueryState,
    chat: Chat?,
    navigateBack: () -> Unit,
    onAddChatMembers: () -> Unit
) {
    TopAppBar(
        title = {
            if (chatQueryState == ChatQueryState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                val name = chat?.Name
                Text(if (name.isNullOrEmpty()) "Chat" else name)
            }
        },
        navigationIcon = {
            if (chatQueryState == ChatQueryState.Success) {
                IconButton(onClick = navigateBack) {
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
                IconButton(onClick = onAddChatMembers) {
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
}

@Composable
private fun ChatBottomBar(
    isEnabled: Boolean,
    onMessage: (String) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(96.dp),
        containerColor = Color.Transparent
    ) {
        ChatBox(
            enabled = isEnabled,
            onMessage = onMessage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ChatContent(
    messages: List<MessageUI>,
    chatId: Int,
    chatViewModel: ChatViewModel,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(reverseLayout = true) {
            var prevMessage: MessageUI? = null
            items(messages) { message ->
                if (prevMessage != null && prevMessage?.date != message.date) {
                    Text(
                        text = message.date,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                prevMessage = message
                MessageItem(message)
            }
            item {
                if (messages.isNotEmpty()) {
                    LaunchedEffect(Unit) { chatViewModel.loadOlderMessages(chatId) }
                }
            }
        }
    }
}