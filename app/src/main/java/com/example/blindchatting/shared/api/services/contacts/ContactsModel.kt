package com.example.blindchatting.shared.api.services.contacts

data class CreateContactRequest(
    val contact_login: String,
)

open class UserInfo(
    open val ID: Int,
    open val Login: String,
    open val UserName: String
)
