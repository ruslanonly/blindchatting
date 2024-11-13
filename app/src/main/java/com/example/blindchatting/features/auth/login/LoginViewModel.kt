package com.example.blindchatting.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.lib.TokenManager
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
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _authState = MutableStateFlow<LoginState>(LoginState.Nothing)
    val authState: StateFlow<LoginState> get() = _authState

    fun login(username: String, password: String) {
        _authState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = authService.login(LoginRequest(username, password))
                val accessToken = response.body()?.Token?.AccessToken
                val refreshToken = response.body()?.Token?.RefreshToken

                if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                    tokenManager.saveTokenPair(accessToken, refreshToken)
                    _authState.value = LoginState.Success
                } else {
                    _authState.value = LoginState.Error("Ошибка авторизации")
                }

            } catch (e: Exception) {
                _authState.value = LoginState.Error("Ошибка авторизации: ${e.message}")
            }
        }
    }
}