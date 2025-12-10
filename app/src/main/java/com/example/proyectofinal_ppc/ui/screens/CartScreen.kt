package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun CartScreen(
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    val state by storeViewModel.state.collectAsState()

    val total = state.cart.sumOf { it.subtotal }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (state.cart.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.cart) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(item.product.name, style = MaterialTheme.typography.titleMedium)
                                Text("Precio unidad: $${item.product.price}")
                                Text(
                                    "Subtotal: $${item.subtotal}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(onClick = {
                                    storeViewModel.updateCartQuantity(
                                        item.product.id,
                                        item.quantity - 1
                                    )
                                }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                                }
                                Text("${item.quantity}")
                                IconButton(onClick = {
                                    storeViewModel.updateCartQuantity(
                                        item.product.id,
                                        item.quantity + 1
                                    )
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Sumar")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleMedium)
                Text("$${total}", style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate(Screen.Checkout.route) },
            enabled = state.cart.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Proceder al pago")
        }
    }
}
