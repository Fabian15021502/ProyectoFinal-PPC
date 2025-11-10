package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Perfil del Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("Nombre: Usuario Ejemplo")
        Text("Correo: usuario@correo.com")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.Settings.route) }) {
            Text("Configuraciones")
        }
    }
}
