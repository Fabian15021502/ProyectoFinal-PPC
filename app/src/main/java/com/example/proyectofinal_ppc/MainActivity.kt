package com.example.proyectofinal_ppc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal_ppc.navigation.NavGraph
import com.example.proyectofinal_ppc.ui.theme.UDShopTheme
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UDShopTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val storeViewModel: StoreViewModel = viewModel()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        storeViewModel = storeViewModel
                    )
                }
            }
        }
    }
}
