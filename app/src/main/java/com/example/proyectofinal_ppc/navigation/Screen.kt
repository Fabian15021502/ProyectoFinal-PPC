package com.example.proyectofinal_ppc.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")

    // Cliente
    object Home : Screen("home") // lista de categorías

    object CategoryProducts : Screen("category_products/{categoryId}") {
        fun createRoute(categoryId: String) = "category_products/$categoryId"
    }

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation")
    object Profile : Screen("profile")
    object Settings : Screen("settings")

    // Admin
    object AdminHome : Screen("admin_home")

    object AdminEditProduct : Screen("admin_edit_product/{productId}") {
        fun createRoute(productId: String) = "admin_edit_product/$productId"
        fun createNew() = "admin_edit_product/new"
    }
}
