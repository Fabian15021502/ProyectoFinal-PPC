package com.example.proyectofinal_ppc.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.CUSTOMER
)
