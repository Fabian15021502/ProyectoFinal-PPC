package com.example.proyectofinal_ppc.ui.screens.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    val state by storeViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        storeViewModel.loadCategoriesAndProducts()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin - Productos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AdminEditProduct.createNew())
            }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(state.products) { product ->
                Card(
                    modifier = Modifier.padding(8.dp),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text("$${product.price}")
                        Text("Stock: ${product.stock}")
                        Row {
                            TextButton(
                                onClick = {
                                    navController.navigate(
                                        Screen.AdminEditProduct.createRoute(product.id)
                                    )
                                }
                            ) { Text("Editar") }

                            TextButton(
                                onClick = { storeViewModel.deleteProduct(product.id) }
                            ) { Text("Eliminar") }
                        }
                    }
                }
            }
        }
    }
}
