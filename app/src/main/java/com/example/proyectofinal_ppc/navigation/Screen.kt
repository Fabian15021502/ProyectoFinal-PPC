package com.example.proyectofinal_ppc.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ProductDetail : Screen("product_detail")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
