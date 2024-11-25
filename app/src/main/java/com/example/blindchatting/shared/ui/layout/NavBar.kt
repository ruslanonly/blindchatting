package com.example.blindchatting.shared.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.blindchatting.shared.routing.Route
import kotlinx.serialization.Serializable

sealed interface NavBarItem {
    @Serializable
    data object Contacts : NavBarItem

    @Serializable
    data object Chats : NavBarItem

    @Serializable
    data object Settings : NavBarItem
}

@Composable
fun NavBar(
    navController: NavController,
    activeItem: NavBarItem,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            label = { Text("Contacts") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Call,
                    contentDescription = "Contacts"
                )
            },
            selected = activeItem == NavBarItem.Contacts,
            onClick = { navController.navigate(Route.Messenger.Contacts) }
        )
        NavigationBarItem(
            label = { Text("Chats") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Chats"
                )
            },
            selected = activeItem == NavBarItem.Chats,
            onClick = { navController.navigate(Route.Messenger.Chatlist) }
        )
        NavigationBarItem(
            label = { Text("Settings") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Settings"
                )
            },
            selected = activeItem == NavBarItem.Settings,
            onClick = { navController.navigate(Route.Messenger.Settings) }
        )
    }
}