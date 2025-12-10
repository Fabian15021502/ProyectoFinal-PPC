package com.example.proyectofinal_ppc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal_ppc.model.UserRole
import com.example.proyectofinal_ppc.ui.screens.*
import com.example.proyectofinal_ppc.ui.screens.admin.AdminEditProductScreen
import com.example.proyectofinal_ppc.ui.screens.admin.AdminHomeScreen
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    storeViewModel: StoreViewModel
) {
    val authState by authViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController, authViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController, authViewModel)
        }

        // Cliente
        composable(Screen.Home.route) {
            HomeScreen(navController, storeViewModel)
        }

        composable(Screen.CategoryProducts.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryProductsScreen(navController, storeViewModel, categoryId)
        }

        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController, storeViewModel, productId)
        }

        composable(Screen.Cart.route) {
            CartScreen(navController, storeViewModel)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController, storeViewModel, authViewModel)
        }

        composable(Screen.OrderConfirmation.route) {
            OrderConfirmationScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, authViewModel)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController, authViewModel)
        }

        // Admin
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(navController, storeViewModel)
        }

        composable(Screen.AdminEditProduct.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: "new"
            AdminEditProductScreen(navController, storeViewModel, productId)
        }
    }
}
