package com.example.proyectofinal_ppc.data

import com.example.proyectofinal_ppc.model.Category
import com.example.proyectofinal_ppc.model.Order
import com.example.proyectofinal_ppc.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // --------- Categorías ---------

    suspend fun getCategories(): List<Category> {
        val snap = db.collection("categories").get().await()
        return snap.documents.mapNotNull { doc ->
            doc.toObject(Category::class.java)?.copy(id = doc.id)
        }
    }

    // --------- Productos ---------

    suspend fun getProducts(): List<Product> {
        val snap = db.collection("products").get().await()
        return snap.documents.mapNotNull { doc ->
            doc.toObject(Product::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun saveProduct(product: Product) {
        val col = db.collection("products")
        if (product.id.isEmpty()) {
            col.add(product).await()
        } else {
            col.document(product.id).set(product).await()
        }
    }

    suspend fun deleteProduct(productId: String) {
        db.collection("products").document(productId).delete().await()
    }

    // --------- Pedidos ---------

    suspend fun createOrder(order: Order) {
        db.collection("orders").add(order).await()
    }
}
