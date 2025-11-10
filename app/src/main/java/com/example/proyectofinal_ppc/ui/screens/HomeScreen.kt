package com.example.proyectofinal_ppc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("UDShop") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                Text("🛒")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(5) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onClick = { navController.navigate(Screen.ProductDetail.route) }
                ) {
                    Text("Producto #$index", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
