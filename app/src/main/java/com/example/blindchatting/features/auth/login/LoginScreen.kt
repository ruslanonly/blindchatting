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
import com.example.blindchatting.shared.routing.AppRoutes
import com.example.blindchatting.shared.routing.LocalAppNavigator
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = getViewModel()) {
    val navController = LocalAppNavigator.current;

    val uiState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginState.Success -> {
                navController.navigate(AppRoutes.ChatList.route)
            }
            is LoginState.Error -> {
                Toast.makeText(context, "Sign In failed", Toast.LENGTH_SHORT).show()
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
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.size(16.dp))

            if (uiState == LoginState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState == LoginState.Nothing)
                ) {
                    Text(text = "Sign In")
                }

                TextButton(onClick = { navController.navigate(AppRoutes.Register.route) }) {
                    Text(text = "Don't have an account? Register")
                }
            }
        }
    }
}