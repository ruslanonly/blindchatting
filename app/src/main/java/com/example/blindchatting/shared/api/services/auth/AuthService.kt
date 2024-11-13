package com.example.blindchatting.shared.api.services.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>
}