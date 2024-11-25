package com.example.blindchatting.features.auth.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.blindchatting.shared.ui.Header
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    val uiState by viewModel.authState.collectAsState()
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginState.Success -> {
                onLoginSuccess()
                Toast.makeText(context, "You've signed in an account", Toast.LENGTH_SHORT).show()
            }
            is LoginState.Error -> {
                Toast.makeText(context, "Login failed: ${uiState.toString()}", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(text = "Login")

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Login") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = { viewModel.login(login, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = (
                    login.isNotEmpty() &&
                    password.isNotEmpty() &&
                    uiState != LoginState.Loading)
            ) {
                if (uiState != LoginState.Loading) {
                    Text(text = "Login", color = Color.White)
                } else {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            TextButton(
                onClick = { onGoToRegister() },
                enabled = uiState != LoginState.Loading
            ) {
                Text(text = "Don't have an account? Register")
            }
        }
    }
}