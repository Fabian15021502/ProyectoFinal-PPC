package com.example.proyectofinal_ppc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.proyectofinal_ppc.ui.screens.*
import com.example.proyectofinal_ppc.ui.screens.admin.AdminHomeScreen
import com.example.proyectofinal_ppc.ui.screens.admin.AdminEditProductScreen
import com.example.proyectofinal_ppc.ui.viewmodel.AuthViewModel
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    storeViewModel: StoreViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Cliente
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                storeViewModel = storeViewModel
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                storeViewModel = storeViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                navController = navController,
                storeViewModel = storeViewModel,
                authViewModel = authViewModel
            )
        }

        composable(Screen.OrderConfirmation.route) {
            OrderConfirmationScreen(navController = navController)
        }

        // Detalle de producto
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                navController = navController,
                storeViewModel = storeViewModel,
                productId = productId
            )
        }

        // Productos por categoría
        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryProductsScreen(
                navController = navController,
                storeViewModel = storeViewModel,
                categoryId = categoryId
            )
        }

        // Admin home
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                navController = navController,
                storeViewModel = storeViewModel
            )
        }

        // Admin crear/editar producto
        composable(
            route = Screen.AdminEditProduct.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            // 👇 SIN remember: leemos el argumento cada vez
            val productId = backStackEntry.arguments?.getString("productId")

            AdminEditProductScreen(
                navController = navController,
                storeViewModel = storeViewModel,
                productId = productId
            )
        }
    }
}
