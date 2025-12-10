package com.example.proyectofinal_ppc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal_ppc.data.StoreRepository
import com.example.proyectofinal_ppc.model.CartItem
import com.example.proyectofinal_ppc.model.Category
import com.example.proyectofinal_ppc.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StoreUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val isOrderInProgress: Boolean = false
)

class StoreViewModel(
    private val repo: StoreRepository = StoreRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(StoreUiState())
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

    // -------- CARGA INICIAL --------

    fun loadCategoriesAndProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val categories = repo.getCategories()
                val products = repo.getProducts()
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories,
                    products = products,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar datos"
                )
            }
        }
    }

    // -------- CATEGORÍAS --------

    /**
     * Crea una categoría y devuelve su ID.
     * Se llama desde AdminEditProductScreen.
     */
    suspend fun createCategory(
        name: String,
        description: String
    ): String {
        return repo.createCategory(name, description)
    }

    // -------- PRODUCTOS (ADMIN) --------

    fun saveProduct(
        id: String?,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        imageUrl: String,
        categoryId: String
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repo.saveProduct(
                    id = id,
                    name = name,
                    description = description,
                    price = price,
                    stock = stock,
                    imageUrl = imageUrl,
                    categoryId = categoryId
                )
                // Recargar listas
                val categories = repo.getCategories()
                val products = repo.getProducts()
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories,
                    products = products
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al guardar producto"
                )
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repo.deleteProduct(productId)
                val categories = repo.getCategories()
                val products = repo.getProducts()
                _state.value = _state.value.copy(
                    isLoading = false,
                    categories = categories,
                    products = products
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al eliminar producto"
                )
            }
        }
    }

    // -------- CARRITO --------

    fun addToCart(product: Product) {
        val current = _state.value.cart.toMutableList()
        val existingIndex = current.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            val newQty = existing.quantity + 1
            current[existingIndex] = existing.copy(
                quantity = newQty,
                subtotal = newQty * product.price
            )
        } else {
            current.add(
                CartItem(
                    product = product,
                    quantity = 1,
                    subtotal = product.price
                )
            )
        }
        _state.value = _state.value.copy(cart = current)
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        val current = _state.value.cart.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            if (newQuantity <= 0) {
                current.removeAt(index)
            } else {
                val item = current[index]
                current[index] = item.copy(
                    quantity = newQuantity,
                    subtotal = newQuantity * item.product.price
                )
            }
            _state.value = _state.value.copy(cart = current)
        }
    }

    fun removeFromCart(productId: String) {
        val current = _state.value.cart.filterNot { it.product.id == productId }
        _state.value = _state.value.copy(cart = current)
    }

    fun clearCart() {
        _state.value = _state.value.copy(cart = emptyList())
    }

    // -------- PEDIDOS --------

    fun createOrder(
        userId: String,
        address: String,
        paymentMethod: String,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val items = _state.value.cart
        if (items.isEmpty()) {
            onError("El carrito está vacío.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isOrderInProgress = true, error = null)
            try {
                val orderId = repo.createOrder(
                    userId = userId,
                    items = items,
                    address = address,
                    paymentMethod = paymentMethod
                )
                clearCart()
                _state.value = _state.value.copy(isOrderInProgress = false)
                onSuccess(orderId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isOrderInProgress = false,
                    error = e.message ?: "Error al crear el pedido"
                )
                onError(e.message ?: "Error al crear el pedido")
            }
        }
    }
}
