package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun CheckoutScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    authViewModel: AuthViewModel
) {
    val storeState by storeViewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()

    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Tarjeta") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Datos de envío", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección completa") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Método de pago")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = paymentMethod,
            onValueChange = { paymentMethod = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Total a pagar: $${storeState.total}")
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                authState.user?.let { user ->
                    storeViewModel.createOrder(user.uid, address, paymentMethod)
                    navController.navigate(Screen.OrderConfirmation.route) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                }
            },
            enabled = address.isNotBlank() && storeState.cart.isNotEmpty()
        ) {
            Text("Confirmar pedido")
        }
    }
}
