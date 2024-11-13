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
import com.example.blindchatting.shared.routing.AppRoutes
import com.example.blindchatting.shared.routing.LocalAppNavigator
import org.koin.androidx.compose.getViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = getViewModel()) {
    val navController = LocalAppNavigator.current;

    val uiState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterState.Success -> {
                navController.navigate(AppRoutes.Login.route)
            }
            is RegisterState.Error -> {
                Toast.makeText(context, "Register failed", Toast.LENGTH_SHORT).show()
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.size(16.dp))

            if (uiState == RegisterState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.register(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState == RegisterState.Nothing)
                ) {
                    Text(text = "Login")
                }

                TextButton(onClick = { navController.navigate(AppRoutes.Login.route) }) {
                    Text(text = "Do you have an account? Login")
                }
            }
        }
    }
}