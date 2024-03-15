package com.dam2jms.factoriafp24.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dam2jms.factoriafp24.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelRegister : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onChange(user: String, password: String, repeatpassword: String){
        _uiState.value = _uiState.value.copy(user = user, password = password, repeat_password = repeatpassword)
    }

    fun registrarUsuario(context: Context) {
        val auth = uiState.value.auth
        val user = uiState.value.user
        val password = uiState.value.password
        val repeatPassword = uiState.value.repeat_password

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length >= 6 && password.equals(repeatPassword)){
            auth.createUserWithEmailAndPassword(user, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Las contraseñas no coinciden o las contraseñas no tienen un tamaño minimo de 6", Toast.LENGTH_SHORT).show()
        }
    }
}
