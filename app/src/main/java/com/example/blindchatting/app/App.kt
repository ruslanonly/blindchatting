package com.example.blindchatting.app

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.blindchatting.features.auth.AuthViewModel
import com.example.blindchatting.shared.routing.Route
import com.example.blindchatting.ui.theme.BlindchattingTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(authViewModel: AuthViewModel = koinViewModel()) {
    val isLoggedIn = authViewModel.isLoggedIn()

    val navController = rememberNavController()

    val startDestination = if (isLoggedIn) Route.Messenger.Chatlist else Route.Auth.Login

    BlindchattingTheme {
        NavRoot(
            navController = navController,
            startDestination = startDestination,
        )
    }
}