package com.example.blindchatting.features.auth.register

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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
    viewModel: RegisterViewModel = getViewModel()
) {
    val uiState by viewModel.authState.collectAsState()
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterState.Success -> {
                onRegisterSuccess()
                Toast.makeText(context, "You've registered an account", Toast.LENGTH_SHORT).show()
            }
            is RegisterState.Error -> {
                Toast.makeText(context, "Register failed: ${uiState.toString()}", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(text = "Create a new account")

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
                label = { Text(text = "Password") }
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Confirm password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = { viewModel.register(login, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = (
                    uiState != RegisterState.Loading &&
                    login.isNotEmpty() &&
                    password.isNotEmpty() &&
                    password == confirmPassword
                )
            ) {
                if (uiState != RegisterState.Loading) {
                    Text(text = "Register", color = Color.White)
                } else {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            TextButton(
                onClick = { onGoToLogin() },
                enabled = uiState != RegisterState.Loading
            ) {
                Text(text = "Do you have an account? Login")
            }
        }
    }
}