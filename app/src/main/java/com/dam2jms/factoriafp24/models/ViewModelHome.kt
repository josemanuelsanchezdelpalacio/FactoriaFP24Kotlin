package com.dam2jms.factoriafp24.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam2jms.factoriafp24.screens.Proyecto
import com.dam2jms.factoriafp24.states.UiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class ViewModelHome : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val db = Firebase.firestore

    fun onChangeAñadir(nombreProyecto: String, descripcion: String, estado: String, contacto: String) {
        _uiState.update { currentState ->
            currentState.copy(nombreProyecto = nombreProyecto, descripcion = descripcion, estado = estado, contacto = contacto)
        }
    }

    fun onChangeEliminar(id: String) {
        _uiState.update { currentState ->
            currentState.copy(id = id)
        }
    }

    fun onChangeModificar(id: String, nombreProyecto: String, descripcion: String, estado: String, contacto: String) {
        _uiState.update { currentState ->
            currentState.copy(id = id, nombreProyecto = nombreProyecto, descripcion = descripcion, estado = estado, contacto = contacto)
        }
    }

    //para actualizar los proyectos cada vez que se hace un cambio y se vuelve a la pantalla principal
    fun actualizarProyectos(proyectos: List<Proyecto>) {
        _uiState.update { currentState ->
            currentState.copy(proyectos = proyectos)
        }
    }

    //para leer todos los proyectos de firestore
    fun leerProyectos(context: Context, onSuccess: (List<Proyecto>) -> Unit) {
        //obtengo la coleccion
        db.collection("proyectos").get().addOnSuccessListener { resultado ->
                //mapeo los documentos objetos a un objeto
                val proyectos = resultado.documents.mapNotNull { document ->
                    //obtengo los datos del documento
                    val datos = document.data
                    //y creo el objeto metiendo los datos a traves de los campos del documento
                    //si alguno esta vacio meto una cadena vacia
                    if (datos != null) {
                        Proyecto(
                            id = datos["id"] as? String ?: "",
                            nombreProyecto = datos["nombreProyecto"] as? String ?: "",
                            descripcion = datos["descripcion"] as? String ?: "",
                            estado = datos["estado"] as? String ?: "",
                            contacto = datos["contacto"] as? String ?: ""
                        )
                    } else {
                        //para obviar algun documento que este vacio
                        null
                    }
                }
                //devuelvo la lista de proyectos
                onSuccess(proyectos)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al leer los proyectos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //para añadir un proyecto con un ID autogenerado
    fun agregarProyecto(proyecto: Proyecto, context: Context) {
        val proyectosRef = db.collection("proyectos")

        //compruebo que el correo sea válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(proyecto.contacto).matches()) {
            Toast.makeText(context, "El campo de contacto debe ser un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        //compruebo que el estado del proyecto sea valido
        val estadosPermitidos = listOf("en activo", "en pausa", "completado")
        if (!estadosPermitidos.contains(proyecto.estado.toLowerCase())) {
            Toast.makeText(context, "El estado del proyecto debe ser 'en activo', 'en pausa', o 'completado'", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                var maxId = 0

                //obtengo el ID mas alto del ultimo de los proyectos
                val query = proyectosRef.get().await()
                for (document in query) {
                    val proyecto = document.toObject(Proyecto::class.java)
                    val id = proyecto.id.toIntOrNull()
                    if (id != null && id > maxId) {
                        maxId = id
                    }
                }
                //incremento el ID para obtener el proximo
                val nuevoId = maxId + 1

                //asigno el ID al proyecto antes de agregarlo
                val proyectoConId = proyecto.copy(id = nuevoId.toString())

                //añado el proyecto con el ID generado al Firestore
                //uso el id como identificador del documento
                proyectosRef.document(nuevoId.toString()).set(proyectoConId).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Proyecto agregado con éxito", Toast.LENGTH_SHORT).show()
                }
            } catch (excepcion: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al agregar el proyecto: ${excepcion.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //para eliminar un proyecto a traves de su id
    fun eliminarProyecto(idProyecto: String, context: Context) {
        db.collection("proyectos").document(idProyecto)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Proyecto eliminado con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al eliminar el proyecto: ${exception.message}", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    }

    fun modificarProyecto(idProyecto: String, proyecto: Proyecto, context: Context) {
        //obtengo el documento del proyecto mediante su ID
        val proyectoRef = db.collection("proyectos").document(idProyecto)

        //compruebo que el correo sea válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(proyecto.contacto).matches()) {
            Toast.makeText(context, "El campo de contacto debe ser un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        //compruebo que el estado del proyecto sea valido
        val estadosPermitidos = listOf("en activo", "en pausa", "completado")
        if (!estadosPermitidos.contains(proyecto.estado.toLowerCase())) {
            Toast.makeText(context, "El estado del proyecto debe ser 'en activo', 'en pausa', o 'completado'", Toast.LENGTH_SHORT).show()
            return
        }

        //actualizo los campos del proyecto con los nuevos valores
        proyectoRef.update(
            mapOf(
                "nombreProyecto" to proyecto.nombreProyecto,
                "descripcion" to proyecto.descripcion,
                "estado" to proyecto.estado,
                "contacto" to proyecto.contacto
            )
        )
            .addOnSuccessListener {
                Toast.makeText(context, "Proyecto modificado con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al modificar el proyecto: ${exception.message}", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    }

}
