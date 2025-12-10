package com.example.proyectofinal_ppc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal_ppc.data.StoreRepository
import com.example.proyectofinal_ppc.model.CartItem
import com.example.proyectofinal_ppc.model.Category
import com.example.proyectofinal_ppc.model.Order
import com.example.proyectofinal_ppc.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StoreState(
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val total: Double get() = cart.sumOf { it.subtotal }
}

class StoreViewModel(
    private val repo: StoreRepository = StoreRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(StoreState())
    val state: StateFlow<StoreState> = _state

    // ------- CARGA DE CATEGORÍAS Y PRODUCTOS -------

    fun loadCategoriesAndProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val categories = repo.getCategories()
                val products = repo.getProducts()
                _state.value = _state.value.copy(
                    categories = categories,
                    products = products,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    // ------- CARRITO -------

    fun addToCart(product: Product) {
        val list = _state.value.cart.toMutableList()
        val idx = list.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            val item = list[idx]
            list[idx] = item.copy(quantity = item.quantity + 1)
        } else {
            list += CartItem(product, 1)
        }
        _state.value = _state.value.copy(cart = list)
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        val updated = _state.value.cart.mapNotNull { item ->
            if (item.product.id == productId) {
                if (quantity <= 0) null else item.copy(quantity = quantity)
            } else item
        }
        _state.value = _state.value.copy(cart = updated)
    }

    fun clearCart() {
        _state.value = _state.value.copy(cart = emptyList())
    }

    // ------- PEDIDOS -------

    fun createOrder(userId: String, address: String, payment: String) {
        viewModelScope.launch {
            val s = _state.value
            val order = Order(
                userId = userId,
                items = s.cart,
                total = s.total,
                address = address,
                paymentMethod = payment
            )
            repo.createOrder(order)
            clearCart()
        }
    }

    // ------- ADMIN (CRUD PRODUCTOS) -------

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            repo.saveProduct(product)
            // recarga productos para que la UI se actualice
            loadCategoriesAndProducts()
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repo.deleteProduct(productId)
            loadCategoriesAndProducts()
        }
    }
}
