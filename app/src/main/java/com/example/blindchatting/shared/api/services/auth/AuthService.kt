package com.example.blindchatting.shared.api.services.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("user/sign-in")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("user/sign-up")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>

    @POST("user/refresh")
    suspend fun refreshTokens(@Body refreshTokensRequest: RefreshTokensRequest): Response<RefreshTokensResponse>

    @POST("user/set/username")
    suspend fun setUsername(@Body setUsernameRequest: SetUsernameRequest): Response<Unit>
}