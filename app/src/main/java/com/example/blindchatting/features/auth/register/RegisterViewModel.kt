package com.example.blindchatting.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.auth.AuthService
import com.example.blindchatting.shared.api.services.auth.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Nothing : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _authState = MutableStateFlow<RegisterState>(RegisterState.Nothing)
    val authState: StateFlow<RegisterState> get() = _authState

    fun register(username: String, password: String) {
        _authState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = authService.login(LoginRequest(username, password))
                val accessToken = response.body()?.Token?.AccessToken
                val refreshToken = response.body()?.Token?.RefreshToken

                if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                    tokenManager.saveTokenPair(accessToken, refreshToken)
                    _authState.value = RegisterState.Success
                } else {
                    _authState.value = RegisterState.Error("Ошибка регистрации")
                }

            } catch (e: Exception) {
                _authState.value = RegisterState.Error("Ошибка регистрации: ${e.message}")
            }
        }
    }
}