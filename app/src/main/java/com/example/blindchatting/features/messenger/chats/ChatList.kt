package com.example.blindchatting.features.messenger.chats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatsLoaded(modifier: Modifier = Modifier) {
//    LazyColumn() {
//        if (chats.loadState.refresh is LoadState.Loading) {
//            item {
//                LinearProgressIndicator(modifier = modifier.fillMaxWidth())
//            }
//        }
//        itemsIndexed(chats) { index, item ->
//            item?.let { chat ->
//                ChatItem(
//                    client,
//                    chat,
//                    modifier = Modifier.clickable(onClick = {
//                        onChatClicked(item.id)
//                    })
//                )
//                Divider(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    thickness = 0.5.dp,
//                    startIndent = 64.dp
//                )
//            }
//        }
//    }
}
