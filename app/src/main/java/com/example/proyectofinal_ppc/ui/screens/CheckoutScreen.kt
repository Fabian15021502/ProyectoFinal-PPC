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

    val total = storeState.cart.sumOf { it.subtotal }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Confirmar pedido", style = MaterialTheme.typography.headlineMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Datos de envío", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección completa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Método de pago", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total a pagar:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "$${total}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                authState.user?.let { user ->
                    storeViewModel.createOrder(
                        userId = user.uid,
                        address = address,
                        paymentMethod = paymentMethod
                    )
                    navController.navigate(Screen.OrderConfirmation.route) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                }
            },
            enabled = address.isNotBlank() && storeState.cart.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar pedido")
        }
    }
}
