package com.example.blindchatting.shared.api.services.contacts

data class CreateContactRequest(
    val contact_login: String,
)

data class UserInfo(
    val ID: Int,
    val Login: String,
    val UserName: String
)
