package com.example.blindchatting.features.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.api.services.EptaChatApi
import com.example.blindchatting.shared.api.services.auth.AuthService
import com.example.blindchatting.shared.api.services.auth.LoginRequest
import com.example.blindchatting.shared.api.services.auth.RegisterRequest
import com.google.gson.JsonParseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class RegisterState {
    object Nothing : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val eptaChatApi: EptaChatApi
) : ViewModel() {

    private val _authState = MutableStateFlow<RegisterState>(RegisterState.Nothing)
    val authState: StateFlow<RegisterState> get() = _authState

    fun register(login: String, password: String) {
        _authState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                Log.d("RegisterViewModel", "Request: ${login} ${password}")

                val response = eptaChatApi.authService.register(RegisterRequest(login, password))
                Log.d("RegisterViewModel", response.toString())

                if (response.isSuccessful) {
                    _authState.value = RegisterState.Success
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Ошибка регистрации"
                    Log.e("RegisterViewModel", "Server error: $errorMessage")
                    _authState.value = RegisterState.Error("Ошибка регистрации: $errorMessage")
                }

            } catch (e: IOException) {
                Log.e("RegisterViewModel", "Network error: ${e.message}", e)
                _authState.value = RegisterState.Error("Ошибка сети: проверьте подключение к интернету.")
            } catch (e: JsonParseException) {
                Log.e("RegisterViewModel", "JSON parse error: ${e.message}", e)
                _authState.value = RegisterState.Error("Ошибка формата данных от сервера.")
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error during registration: ${e.message}", e)
                _authState.value = RegisterState.Error("Ошибка регистрации: ${e.message}")
            }
        }
    }
}