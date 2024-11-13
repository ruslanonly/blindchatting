package com.example.blindchatting.shared.routing

sealed class AppRoutes(val route: String) {
    object Contacts : AppRoutes("contacts")
    object ChatList : AppRoutes("chat-list")
    object Chat : AppRoutes("chat")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
}
