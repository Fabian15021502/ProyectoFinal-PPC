package com.example.proyectofinal_ppc.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")

    // Cliente
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation")

    // Detalle de producto
    object ProductDetail : Screen("product_detail?productId={productId}") {
        fun createRoute(productId: String) =
            "product_detail?productId=$productId"
    }

    // Productos por categoría
    object CategoryProducts : Screen("category_products?categoryId={categoryId}") {
        fun createRoute(categoryId: String) =
            "category_products?categoryId=$categoryId"
    }

    // Admin
    object AdminHome : Screen("admin_home")

    object AdminEditProduct : Screen("admin_edit_product?productId={productId}") {
        fun createRoute(productId: String) =
            "admin_edit_product?productId=$productId"

        fun createNew() =
            "admin_edit_product"   // sin parámetro → producto nuevo
    }
}
