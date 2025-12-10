package com.example.proyectofinal_ppc.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.model.Product
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun AdminEditProductScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    productId: String
) {
    val state by storeViewModel.state.collectAsState()
    val existingProduct = state.products.find { it.id == productId }

    var name by remember { mutableStateOf(existingProduct?.name ?: "") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var price by remember { mutableStateOf(existingProduct?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(existingProduct?.stock?.toString() ?: "") }
    var categoryId by remember { mutableStateOf(existingProduct?.categoryId ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            if (existingProduct == null) "Nuevo producto" else "Editar producto",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = categoryId,
            onValueChange = { categoryId = it },
            label = { Text("Id categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val product = Product(
                id = existingProduct?.id ?: "",
                name = name,
                description = description,
                price = price.toDoubleOrNull() ?: 0.0,
                stock = stock.toIntOrNull() ?: 0,
                categoryId = categoryId,
                available = true
            )
            storeViewModel.saveProduct(product)
            navController.popBackStack()
        }) {
            Text("Guardar")
        }
    }
}
