package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.model.UserRole
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val state by authViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
        delay(1500)

        val user = state.user
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            val startRoute =
                if (user.role == UserRole.ADMIN) Screen.AdminHome.route else Screen.Home.route

            navController.navigate(startRoute) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("UDShop", style = MaterialTheme.typography.headlineMedium)
    }
}
