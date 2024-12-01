package com.example.blindchatting.shared.api.services.chat

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface ChatService {
    fun connect(chatId: Int, listener: WebSocketListener): WebSocket
    fun disconnect(webSocket: WebSocket)
    fun sendMessage(webSocket: WebSocket, message: String)
}

class ChatServiceImpl(
    private val okHttpClient: OkHttpClient,
    private val baseUrl: String
) : ChatService {
    override fun connect(chatId: Int, listener: WebSocketListener): WebSocket {
        val request = Request.Builder()
            .url(baseUrl + "messenger/connect?chat-id=$chatId")
            .build()
        return okHttpClient.newWebSocket(request, listener)
    }

    override fun disconnect(webSocket: WebSocket) {
        webSocket.close(1000, "Disconnecting")
    }

    override fun sendMessage(webSocket: WebSocket, message: String) {
        webSocket.send(message)
    }
}