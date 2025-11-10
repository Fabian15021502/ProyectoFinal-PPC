package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Iniciar Sesión") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(Screen.Home.route) }) {
                    Text("Entrar")
                }
                TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                    Text("Crear cuenta")
                }
            }
        }
    )
}
