package com.example.proyectofinal_ppc.data

import com.example.proyectofinal_ppc.model.CartItem
import com.example.proyectofinal_ppc.model.Category
import com.example.proyectofinal_ppc.model.Order
import com.example.proyectofinal_ppc.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // -------- CATEGORIES --------

    suspend fun getCategories(): List<Category> {
        val snap = db.collection("categories").get().await()
        return snap.documents.map { doc ->
            val cat = doc.toObject(Category::class.java)
            cat?.copy(id = doc.id) ?: Category(id = doc.id)
        }
    }

    /**
     * Crea una categoría y devuelve el ID del documento.
     */
    suspend fun createCategory(
        name: String,
        description: String
    ): String {
        val docRef = db.collection("categories").document()
        val data = hashMapOf(
            "name" to name,
            "description" to description,
            "imageUrl" to ""
        )
        docRef.set(data).await()
        return docRef.id
    }

    // -------- PRODUCTS --------

    suspend fun getProducts(): List<Product> {
        val snap = db.collection("products").get().await()
        return snap.documents.map { doc ->
            val p = doc.toObject(Product::class.java)
            p?.copy(id = doc.id) ?: Product(id = doc.id)
        }
    }

    /**
     * Guarda un producto.
     * - Si id == null → crea nuevo.
     * - Si id != null → actualiza.
     */
    suspend fun saveProduct(
        id: String?,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        imageUrl: String,
        categoryId: String
    ) {
        val data = hashMapOf(
            "name" to name,
            "description" to description,
            "price" to price,
            "stock" to stock,
            "imageUrl" to imageUrl,
            "available" to (stock > 0),
            "categoryId" to categoryId
        )

        val col = db.collection("products")
        val docRef = if (id == null) col.document() else col.document(id)
        docRef.set(data).await()
    }

    suspend fun deleteProduct(productId: String) {
        db.collection("products").document(productId).delete().await()
    }

    // -------- ORDERS --------

    suspend fun createOrder(
        userId: String,
        items: List<CartItem>,
        address: String,
        paymentMethod: String
    ): String {
        val total = items.sumOf { it.subtotal }
        val ordersCol = db.collection("orders")
        val docRef = ordersCol.document()

        val order = Order(
            id = docRef.id,
            userId = userId,
            items = items,
            total = total,
            address = address,
            paymentMethod = paymentMethod,
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        )

        docRef.set(order).await()
        return docRef.id
    }
}
