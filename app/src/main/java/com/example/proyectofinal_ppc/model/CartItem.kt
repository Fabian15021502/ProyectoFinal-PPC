package com.example.proyectofinal_ppc.model

data class CartItem(
    val product: Product = Product(),
    val quantity: Int = 1,
    val subtotal: Double = 0.0,
)

