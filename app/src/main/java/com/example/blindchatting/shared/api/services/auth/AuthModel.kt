package com.example.blindchatting.shared.api.services.auth

data class LoginRequest(
    val login: String,
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
    val login: String,
    val password: String
)

data class RefreshTokensRequest(
    val token: String,
    val user_id: Int
)

data class RefreshTokensResponse(
    val access_token: String,
)

data class SetUsernameRequest (
    val username: String
);