package com.example.blindchatting.features.messenger.contacts.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactModal(
    isLoading: Boolean = false,
    onCreateContact: (contactLogin: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var contactLogin by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create new contact!",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center

            )

            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                enabled = !isLoading,
                value = contactLogin,
                onValueChange = { contactLogin = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Login")
                }
            )

            Spacer(Modifier.size(16.dp))

            FilledTonalButton(
                enabled = contactLogin.isNotEmpty() && !isLoading,
                onClick = { onCreateContact(contactLogin) },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Create")
                }
            }

            Spacer(Modifier.size(16.dp))
        }
    }
}