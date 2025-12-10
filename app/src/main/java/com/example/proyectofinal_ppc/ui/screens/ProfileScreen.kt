package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val state by authViewModel.state.collectAsState()
    val user = state.user

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Perfil del Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("Nombre: ${user?.name ?: "—"}")
        Text("Correo: ${user?.email ?: "—"}")
        Text("Rol: ${user?.role ?: "—"}")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.Settings.route) }) {
            Text("Configuraciones")
        }
    }
}
