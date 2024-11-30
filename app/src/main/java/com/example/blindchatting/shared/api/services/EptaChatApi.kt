package com.example.blindchatting.shared.api.services

import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.auth.AuthService
import com.example.blindchatting.shared.api.services.auth.ContactsService
import com.example.blindchatting.shared.api.services.auth.RefreshTokensRequest
import com.example.blindchatting.shared.api.services.chats.ChatsService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val eptaChatApi: EptaChatApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(request)

        if (response.code == 401) {
            val newAccessToken = refreshAccessToken()
            if (newAccessToken != null) {
                request = request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                response.close()
                response = chain.proceed(request)
            }
        }

        return response
    }

    private fun refreshAccessToken(): String? {
        val refreshToken = tokenManager.getRefreshToken()
        val userId = tokenManager.getUserId()

        if (!refreshToken.isNullOrEmpty() && userId != 0) {
            val refreshResponse = runBlocking {
                eptaChatApi.authService.refreshTokens(
                    RefreshTokensRequest(token = refreshToken, user_id = userId)
                )
            }

            return if (refreshResponse.isSuccessful) {
                val newTokens = refreshResponse.body()
                newTokens?.let {
                    tokenManager.saveAccessToken(it.access_token)
                    it.access_token
                }
            } else {
                null
            }
        }

        return null
    }
}

class EptaChatApi(private val tokenManager: TokenManager) {
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager, this))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://193.124.33.25:8080/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val contactsService: ContactsService by lazy {
        retrofit.create(ContactsService::class.java)
    }

    val chatsService: ChatsService by lazy {
        retrofit.create(ChatsService::class.java)
    }
}