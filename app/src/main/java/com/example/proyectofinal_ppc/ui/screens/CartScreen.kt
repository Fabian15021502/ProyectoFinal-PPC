package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.cart) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.product.name)
                        Text("Precio: $${item.product.price}")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            storeViewModel.updateCartQuantity(
                                item.product.id,
                                item.quantity - 1
                            )
                        }) { Text("-") }
                        Text("${item.quantity}")
                        IconButton(onClick = {
                            storeViewModel.updateCartQuantity(
                                item.product.id,
                                item.quantity + 1
                            )
                        }) { Text("+") }
                    }
                }
            }
        }

        Text("Total: $${state.total}")
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate(Screen.Checkout.route) },
            enabled = state.cart.isNotEmpty()
        ) {
            Text("Proceder al pago")
        }
    }
}
