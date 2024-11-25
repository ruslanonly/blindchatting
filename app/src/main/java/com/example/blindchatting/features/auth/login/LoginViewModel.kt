package com.example.blindchatting.features.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.auth.AuthService
import com.example.blindchatting.shared.api.services.auth.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Nothing : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val eptaChatApi: EptaChatApi,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _authState = MutableStateFlow<LoginState>(LoginState.Nothing)
    val authState: StateFlow<LoginState> get() = _authState

    fun login(username: String, password: String) {
        _authState.value = LoginState.Loading
        Log.d("LoginViewModel", "Attempting to log in with username: $username")

        viewModelScope.launch {
            try {
                val response = eptaChatApi.authService.login(LoginRequest(username, password))
                Log.d("LoginViewModel", "Response received: ${response.body()}")

                val userId = response.body()?.UserId
                val accessToken = response.body()?.Token?.AccessToken
                val refreshToken = response.body()?.Token?.RefreshToken

                if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty() && userId != null) {
                    tokenManager.saveUserId(userId)
                    tokenManager.saveAccessToken(accessToken)
                    tokenManager.saveRefreshToken(refreshToken)

                    _authState.value = LoginState.Success
                } else {
                    _authState.value = LoginState.Error("Ошибка авторизации")
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error during login: ${e.message}", e)
                _authState.value = LoginState.Error("Произошла ошибка: ${e.message}")
            }
        }
    }
}