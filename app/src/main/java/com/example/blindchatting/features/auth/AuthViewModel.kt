package com.example.blindchatting.features.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.blindchatting.features.auth.login.LoginState
import com.example.blindchatting.shared.api.lib.TokenManager

class AuthViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    fun isLoggedIn(): Boolean {
        Log.d("AuthViewModel", "Attempting to check if user is logged in")

        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            Log.d("AuthViewModel", "User is logged in")

            return true
        } else {
            Log.d("AuthViewModel", "User isn't logged in")

            return false
        }
    }
}