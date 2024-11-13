package com.example.blindchatting

import com.example.blindchatting.features.auth.login.LoginViewModel
import com.example.blindchatting.features.auth.register.RegisterViewModel
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.auth.AuthService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val AppModule = module {
    single { TokenManager(androidContext()) }
    single { createRetrofitInstance() }
    single { createAuthService(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
}

fun createRetrofitInstance(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://your-api-url.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createAuthService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
}
