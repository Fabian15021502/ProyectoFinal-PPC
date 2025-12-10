package com.example.proyectofinal_ppc.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val address: String = "",
    val paymentMethod: String = "",
    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)
