package com.example.blindchatting.features.auth.logout

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.blindchatting.shared.api.lib.TokenManager

class LogoutViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    fun logout(): Boolean {
        Log.d("LogoutViewModel", "Attempting to logout")

        tokenManager.clearTokenPair()

        return true
    }
}