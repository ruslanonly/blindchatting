package com.example.blindchatting.shared.routing

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalAppNavigator = compositionLocalOf<NavHostController> { error("No AppNavigation provided") }