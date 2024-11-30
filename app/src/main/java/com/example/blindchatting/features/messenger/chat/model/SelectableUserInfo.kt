package com.example.blindchatting.features.messenger.chat.model

import com.example.blindchatting.shared.api.services.contacts.UserInfo

data class SelectableUserInfo(
    val isSelected: Boolean = false, // New property for selection state
    override val ID: Int,
    override val Login: String,
    override val UserName: String
) : UserInfo(ID, Login, UserName)