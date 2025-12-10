package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
fun CategoryProductsScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    categoryId: String
) {
    val state by storeViewModel.state.collectAsState()
    val products = remember(state.products, categoryId) {
        state.products.filter { it.categoryId == categoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                Text("🛒")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = {
                        navController.navigate(
                            Screen.ProductDetail.createRoute(product.id)
                        )
                    }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text("$${product.price}")
                    }
                }
            }
        }
    }
}
