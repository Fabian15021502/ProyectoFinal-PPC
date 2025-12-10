package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    productId: String
) {
    val state by storeViewModel.state.collectAsState()
    val product = state.products.find { it.id == productId }

    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Producto no encontrado")
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Detalle del Producto", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text(product.name, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(product.description)
        Spacer(Modifier.height(8.dp))
        Text("Precio: $${product.price}")
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            storeViewModel.addToCart(product)
            navController.navigate(Screen.Cart.route)
        }) {
            Text("Agregar al Carrito")
        }
    }
}
