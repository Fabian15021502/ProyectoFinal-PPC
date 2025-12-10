package com.example.proyectofinal_ppc.data

import android.util.Log
import com.example.proyectofinal_ppc.model.User
import com.example.proyectofinal_ppc.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthRepository"

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun currentUserId(): String? = auth.currentUser?.uid

    /**
     * Carga el usuario actual desde la colección "users".
     * Si hay error (sin internet, permisos, etc.), devuelve null y NO revienta la app.
     */
    suspend fun getCurrentUser(): User? {
        val uid = currentUserId() ?: return null
        return try {
            val snap = db.collection("users").document(uid).get().await()
            snap.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo usuario actual: ${e.message}", e)
            null
        }
    }

    /**
     * Login:
     * 1. Autentica con Firebase Auth.
     * 2. Intenta leer el doc en "users".
     * 3. Si NO existe, lo crea automáticamente como CUSTOMER.
     */
    suspend fun login(email: String, password: String): User {
        auth.signInWithEmailAndPassword(email, password).await()
        val uid = currentUserId() ?: throw IllegalStateException("UID nulo después de login")

        return try {
            val docRef = db.collection("users").document(uid)
            val snap = docRef.get().await()
            val existing = snap.toObject(User::class.java)
            if (existing != null) {
                existing
            } else {
                // Si se logueó pero no tiene doc en "users", lo creamos por defecto
                val newUser = User(
                    uid = uid,
                    name = email.substringBefore("@"),
                    email = email,
                    role = UserRole.CUSTOMER
                )
                docRef.set(newUser).await()
                newUser
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error leyendo/creando user en Firestore: ${e.message}", e)
            // Re-lanzamos para que el ViewModel muestre el error en pantalla
            throw e
        }
    }

    /**
     * Registro:
     * Crea usuario en Auth + documento en "users" como CUSTOMER.
     */
    suspend fun register(name: String, email: String, password: String): User {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw IllegalStateException("UID nulo después de registro")

        val user = User(
            uid = uid,
            name = name,
            email = email,
            role = UserRole.CUSTOMER
        )

        try {
            db.collection("users").document(uid).set(user).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error guardando user en Firestore: ${e.message}", e)
            // si aquí falla, igual devolvemos el user para que el flujo continúe,
            // pero puedes decidir lanzar excepción si quieres forzar que exista en BD.
        }

        return user
    }

    fun logout() {
        auth.signOut()
    }
}
