package com.example.proyectofinal_ppc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal_ppc.data.AuthRepository
import com.example.proyectofinal_ppc.model.User
import com.example.proyectofinal_ppc.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    val isLoggedIn: Boolean get() = _state.value.user != null
    val role: UserRole? get() = _state.value.user?.role

    fun loadCurrentUser() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val user = repo.getCurrentUser()
                _state.value = AuthState(isLoading = false, user = user, error = null)
            } catch (e: Exception) {
                _state.value = AuthState(
                    isLoading = false,
                    user = null,
                    error = e.message ?: "Error al cargar usuario actual"
                )
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                val user = repo.login(email, password)
                _state.value = AuthState(user = user)
            } catch (e: Exception) {
                _state.value = AuthState(error = e.message)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                val user = repo.register(name, email, password)
                _state.value = AuthState(user = user)
            } catch (e: Exception) {
                _state.value = AuthState(error = e.message)
            }
        }
    }

    fun logout() {
        repo.logout()
        _state.value = AuthState()
    }
}
