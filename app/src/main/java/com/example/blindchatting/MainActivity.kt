package com.example.blindchatting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blindchatting.ui.theme.BlindchattingTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home

sealed class NavRoutes(val route: String) {
    object ChatList : NavRoutes("chat-list")
    object Chat : NavRoutes("chat")
}

@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            label = { Text("Chats") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Chat List"
                )
            },
            selected = currentRoute == NavRoutes.ChatList.route,
            onClick = { navController.navigate(NavRoutes.ChatList.route) }
        )
        NavigationBarItem(
            label = { Text("Profile") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile"
                )
            },
            selected = currentRoute == NavRoutes.Chat.route,
            onClick = { navController.navigate(NavRoutes.Chat.route) }
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BlindchattingTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.ChatList.route
                    ) {
                        composable(NavRoutes.ChatList.route) {
                            Text(
                                text = "My Chats",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        composable(NavRoutes.Chat.route) {
                            Text(
                                text = "My Chat",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}