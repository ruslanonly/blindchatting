package com.example.blindchatting.features.messenger.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blindchatting.shared.ui.layout.NavBar
import com.example.blindchatting.shared.ui.layout.NavBarItem

@Composable
fun ChatsScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavBar(
                navController = navController,
                activeItem = NavBarItem.Chats
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chats",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}