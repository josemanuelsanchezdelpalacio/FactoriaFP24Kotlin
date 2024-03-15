package com.dam2jms.factoriafp24.models

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dam2jms.factoriafp24.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelLogin : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var isLoggedIn = MutableStateFlow(false)

    fun onChange(user: String, password: String){
        _uiState.update { currentState -> currentState.copy(user = user, password = password) }
    }

    fun iniciarSesion(context: Context) {
        val auth = uiState.value.auth
        val user = uiState.value.user
        val password = uiState.value.password

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(user, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isLoggedIn.value = true
                Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show()
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("isLoggedIn", true).apply()
            } else {
                Toast.makeText(context, "Error al introducir el usuario o contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}