package com.example.blindchatting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blindchatting.features.auth.login.LoginScreen
import com.example.blindchatting.features.auth.register.RegisterScreen
import com.example.blindchatting.shared.routing.AppRoutes
import com.example.blindchatting.shared.routing.LocalAppNavigator
import com.example.blindchatting.ui.theme.BlindchattingTheme

@Composable
fun NavBar(
    modifier: Modifier = Modifier
) {
    val navController = LocalAppNavigator.current
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
            selected = currentRoute == AppRoutes.ChatList.route,
            onClick = { navController.navigate(AppRoutes.ChatList.route) }
        )
        NavigationBarItem(
            label = { Text("Profile") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile"
                )
            },
            selected = currentRoute == AppRoutes.Chat.route,
            onClick = { navController.navigate(AppRoutes.Chat.route) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    val startDestination = if (false) AppRoutes.ChatList.route else AppRoutes.Login.route

    BlindchattingTheme {
        CompositionLocalProvider(LocalAppNavigator provides navController) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Blindchatting")
                        },
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                    )
                },
                bottomBar = {
                    NavBar()
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    Modifier.padding(paddingValues) // Apply padding from Scaffold
                ) {
                    composable(AppRoutes.Login.route) {
                        LoginScreen()
                    }
                    composable(AppRoutes.Register.route) {
                        RegisterScreen()
                    }
                    composable(AppRoutes.ChatList.route) {
                        Text(
                            text = "My Chats",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    composable(AppRoutes.Chat.route) {
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