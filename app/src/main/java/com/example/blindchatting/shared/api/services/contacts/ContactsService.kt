package com.example.blindchatting.shared.api.services.auth

import com.example.blindchatting.shared.api.services.contacts.CreateContactRequest
import com.example.blindchatting.shared.api.services.contacts.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactsService {
    @POST("contact")
    suspend fun createOne(@Body createContactRequest: CreateContactRequest): Response<Unit>

    @GET("contact/all")
    suspend fun getAll(): Response<Array<UserInfo>>

    @GET("contact/{id}")
    suspend fun get(
        @Path("id") id: Int,
    ): Response<UserInfo>

    @DELETE("contact/{id}")
    suspend fun delete(
        @Path("id") id: Int,
    ): Response<Unit>
}