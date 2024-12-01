package com.example.blindchatting.app

import com.example.blindchatting.features.auth.AuthViewModel
import com.example.blindchatting.features.auth.login.LoginViewModel
import com.example.blindchatting.features.auth.logout.LogoutViewModel
import com.example.blindchatting.features.auth.register.RegisterViewModel
import com.example.blindchatting.features.messenger.chat.ChatViewModel
import com.example.blindchatting.features.messenger.chat.lib.MessageUIMapper
import com.example.blindchatting.features.messenger.chat.ui.add_member.AddChatMembersViewModel
import com.example.blindchatting.features.messenger.chats.all.ChatsViewModel
import com.example.blindchatting.features.messenger.chats.create.CreateChatViewModel
import com.example.blindchatting.features.messenger.contacts.all.ContactsViewModel
import com.example.blindchatting.features.messenger.contacts.create.CreateContactViewModel
import com.example.blindchatting.features.messenger.contacts.delete.DeleteContactViewModel
import com.example.blindchatting.features.messenger.contacts.one.GetContactViewModel
import com.example.blindchatting.features.messenger.settings.username.SetUsernameViewModel
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.EptaChatApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { TokenManager(androidContext()) }
    single { EptaChatApi(get()) }
    single { MessageUIMapper(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LogoutViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ContactsViewModel(get()) }
    viewModel { CreateContactViewModel(get()) }
    viewModel { DeleteContactViewModel(get()) }
    viewModel { GetContactViewModel(get()) }
    viewModel { SetUsernameViewModel(get()) }
    viewModel { ChatsViewModel(get()) }
    viewModel { AddChatMembersViewModel(get()) }
    viewModel { CreateChatViewModel(get()) }
    viewModel { ChatViewModel(get(), get()) }
}
