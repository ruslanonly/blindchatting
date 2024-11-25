package com.example.blindchatting.shared.routing

import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface Auth {
        @Serializable
        data object Login : Auth
        @Serializable
        data object Register : Auth
    }

    sealed interface Messenger {
        @Serializable
        data object Settings : Messenger
        @Serializable
        data object Contacts : Messenger
        @Serializable
        data object Chatlist : Messenger
        @Serializable
        data class Chat(
            val chatId: Int
        ) : Messenger
    }
}
