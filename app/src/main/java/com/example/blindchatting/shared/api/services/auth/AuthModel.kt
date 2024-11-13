package com.example.blindchatting.shared.api.services.auth

data class LoginRequest(
    val username: String,
    val password: String
)

data class TokenPair(
    val AccessToken: String,
    val RefreshToken: String
)

data class LoginResponse(
    val UserId: Int,
    val Token: TokenPair
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RefreshTokensRequest(
    val token: String,
    val userId: Number
)