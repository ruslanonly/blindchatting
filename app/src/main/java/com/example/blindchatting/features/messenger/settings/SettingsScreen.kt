package com.example.blindchatting.features.messenger.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blindchatting.features.auth.logout.LogoutViewModel
import com.example.blindchatting.features.messenger.contacts.delete.DeleteContactState
import com.example.blindchatting.features.messenger.contacts.one.GetContactState
import com.example.blindchatting.features.messenger.contacts.one.GetContactViewModel
import com.example.blindchatting.features.messenger.settings.username.SetUsernameModal
import com.example.blindchatting.features.messenger.settings.username.SetUsernameQueryState
import com.example.blindchatting.features.messenger.settings.username.SetUsernameViewModel
import com.example.blindchatting.shared.api.lib.TokenManager
import com.example.blindchatting.shared.ui.Avatar
import com.example.blindchatting.shared.ui.layout.NavBar
import com.example.blindchatting.shared.ui.layout.NavBarItem
import com.example.blindchatting.shared.ui.Header
import com.example.blindchatting.shared.ui.Placeholder
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            NavBar(
                navController = navController,
                activeItem = NavBarItem.Settings
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Settings(onLogout)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    onLogout: () -> Unit,
    logoutViewModel: LogoutViewModel = koinViewModel(),
    getContactViewModel: GetContactViewModel = koinViewModel(),
    setUsernameViewModel: SetUsernameViewModel = koinViewModel()
) {
    val tokenManager: TokenManager = getKoin().get()

    val context = LocalContext.current

    var showChangeUsernameSheet by remember { mutableStateOf(false) }

    fun logout() {
        val logoutResult = logoutViewModel.logout()

        if (logoutResult) {
            Toast.makeText(
                context,
                "You've successfully logouted from an account",
                Toast.LENGTH_LONG
            ).show()
            onLogout()
        }
    }

    LaunchedEffect(Unit) {
        getContactViewModel.getContact(tokenManager.getUserId())
    }

    val setUsernameState = setUsernameViewModel.setUsernameQuery.collectAsState().value

    LaunchedEffect(setUsernameState) {
        when (setUsernameState) {
            is SetUsernameQueryState.Success -> {
                Toast.makeText(context, "You've changed your username", Toast.LENGTH_SHORT).show()
                getContactViewModel.getContact(tokenManager.getUserId())
                showChangeUsernameSheet = false
            }
            is SetUsernameQueryState.Error -> {
                val message = setUsernameState.message

                Toast.makeText(context, "Error while updating your username: $message", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    val contactState = getContactViewModel.contactState.collectAsState().value

    Column {
        when (contactState) {
            is GetContactState.Loading -> {
                LinearProgressIndicator(
                    Modifier.fillMaxWidth()
                )
            }

            is GetContactState.Success -> {
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Avatar(text = contactState.contact.UserName)

                            Header(
                                text = contactState.contact.UserName,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Setting(
                            icon = Icons.Outlined.Face,
                            text = "Set your username",
                            onClick = {
                                showChangeUsernameSheet = true;
                            }
                        )

                        Setting(
                            icon = Icons.Outlined.Logout,
                            text = "Logout from account",
                            color = MaterialTheme.colorScheme.error,
                            onClick = { logout() }
                        )
                    }
                }
                if (showChangeUsernameSheet) {
                    SetUsernameModal(
                        onDismissRequest = {
                            showChangeUsernameSheet = false
                        },
                        isLoading = setUsernameState == SetUsernameQueryState.Loading,
                        initialUsername = contactState.contact.UserName,
                        onSetUsername = {
                            username -> setUsernameViewModel.setUsername(username)
                        },
                    )
                }
            }

            is GetContactState.Error -> {
                Placeholder(
                    icon = Icons.Outlined.ErrorOutline,
                    color = MaterialTheme.colorScheme.error,
                    message = "Error: contactState.message"
                )
            }
        }
    }
}

@Composable
private fun Setting(
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = color
        )
    }
}
