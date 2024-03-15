package com.dam2jms.factoriafp24.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam2jms.factoriafp24.screens.Proyecto
import com.dam2jms.factoriafp24.states.UiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ViewModelHome : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val db = Firebase.firestore

    fun onChangeAñadir(nombreProyecto: String, descripcion: String, estado: String, contacto: String) {
        _uiState.update { currentState ->
            currentState.copy(nombreProyecto = nombreProyecto, descripcion = descripcion, estado = estado, contacto = contacto)
        }
    }

    fun onChangeEliminar(nombreProyecto: String) {
        _uiState.update { currentState ->
            currentState.copy(nombreProyecto = nombreProyecto)
        }
    }

    fun onChangeModificar(nombreProyecto: String, descripcion: String, estado: String, contacto: String) {
        _uiState.update { currentState ->
            currentState.copy(nombreProyecto = nombreProyecto, descripcion = descripcion, estado = estado, contacto = contacto)
        }
    }

    suspend fun leerProyectos() {
        try {
            val proyectosSnapshot = db.collection("proyectos").get().await()
            val proyectos = proyectosSnapshot.toObjects<Proyecto>()
            _uiState.value = _uiState.value.copy(proyectos = proyectos)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun agregarProyecto(proyecto: Proyecto, context: Context) {
        //compruebo que el contacto solo pueda ser un correo
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(proyecto.contacto).matches()) {
            Toast.makeText(context, "El campo de contacto debe ser un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        //compruebo el estado del proyecto para que sea una de estas 3 opciones
        val estadosPermitidos = listOf("en activo", "en pausa", "completado")
        if (!estadosPermitidos.contains(proyecto.estado.toLowerCase())) {
            Toast.makeText(context, "El estado del proyecto debe ser 'en activo', 'en pausa', o 'completado'", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("proyectos")
            .add(proyecto)
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun eliminarProyecto(nombreProyecto: String, context: Context) {
        db.collection("proyectos").document(nombreProyecto)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Proyecto eliminado con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al eliminar el proyecto", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    }

    fun modificarProyecto(proyecto: Proyecto, context: Context) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(proyecto.contacto).matches()) {
            Toast.makeText(context, "El campo de contacto debe ser un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        val estadosPermitidos = listOf("en activo", "en pausa", "completado")
        if (!estadosPermitidos.contains(proyecto.estado.toLowerCase())) {
            Toast.makeText(context, "El estado del proyecto debe ser 'en activo', 'en pausa', o 'completado'", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("proyectos").document(proyecto.nombreProyecto)
            .set(proyecto)
            .addOnSuccessListener {
                Toast.makeText(context, "Proyecto modificado con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al modificar el proyecto", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    }
}
