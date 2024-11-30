package com.example.blindchatting.shared.api.services.chats

import com.example.blindchatting.shared.api.services.contacts.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatsService {
    @POST("chat")
    suspend fun create(@Body createChatRequest: CreateChatRequest): Response<CreateChatResponse>

    @POST("chat/add/members")
    suspend fun addChatMembers(@Body addChatMembersRequest: AddChatMembersRequest): Response<Unit>

    @GET("chat/all")
    suspend fun getAll(): Response<Array<Chat>>

    @GET("chat/members/{id}")
    suspend fun getChatMembers(@Path("id") id: Int): Response<Array<UserInfo>>

    @GET("chat/members/{id}")
    suspend fun getChatMessages(
        @Query("chat-id") chatId: Int,
        @Query("page-id") pageIndex: Int)
    : Response<Array<MessageInfo>>

    @GET("chat/{id}")
    suspend fun getChat(@Path("id") id: Int): Response<Chat>

    @DELETE("chat/{id}")
    suspend fun deleteChat(
        @Path("id") id: Int,
    ): Response<Unit>
}