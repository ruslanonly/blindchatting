package com.example.blindchatting.app

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.blindchatting.features.auth.login.LoginScreen
import com.example.blindchatting.features.auth.register.RegisterScreen
import com.example.blindchatting.features.messenger.chat.ChatScreen
import com.example.blindchatting.features.messenger.contacts.ChatsScreen
import com.example.blindchatting.features.messenger.contacts.ContactsScreen
import com.example.blindchatting.features.messenger.settings.SettingsScreen
import com.example.blindchatting.shared.routing.Route

@Composable
fun NavRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Route.Auth.Login>() {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Route.Messenger.Chatlist) },
                onGoToRegister = { navController.navigate(Route.Auth.Register) }
            )
        }
        composable<Route.Auth.Register> {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Route.Auth.Login) },
                onGoToLogin = { navController.navigate(Route.Auth.Login) }
            )
        }
        composable<Route.Messenger.Chatlist> {
            ChatsScreen(navController)
        }
        composable<Route.Messenger.Chat> { backStackEntry ->
            val arguments: Route.Messenger.Chat = backStackEntry.toRoute()
            ChatScreen(
                chatId = arguments.chatId,
                navigateBack = { navController.navigate(Route.Messenger.Chatlist)}
            )
        }
        composable<Route.Messenger.Contacts> {
            ContactsScreen(navController)
        }
        composable<Route.Messenger.Settings> { backStackEntry ->
            SettingsScreen(
                onLogout = { navController.navigate(Route.Auth.Login) },
                navController
            )
        }
    }
}