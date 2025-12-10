package com.example.proyectofinal_ppc.ui.screens

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
fun HomeScreen(
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    val state by storeViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        storeViewModel.loadCategoriesAndProducts()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("UDShop - Categorías") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                Text("🛒")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(padding).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(state.categories) { category ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp),
                        onClick = {
                            navController.navigate(
                                Screen.CategoryProducts.createRoute(category.id)
                            )
                        }
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
