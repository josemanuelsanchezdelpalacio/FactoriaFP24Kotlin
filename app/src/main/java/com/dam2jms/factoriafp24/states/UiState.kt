package com.dam2jms.factoriafp24.states

import com.dam2jms.factoriafp24.screens.Proyecto
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

data class UiState(
    var user: String = "",
    var password: String = "",
    var repeat_password: String = "",
    val auth: FirebaseAuth = Firebase.auth,

    val proyectos: List<Proyecto> = emptyList(),
    var nombreProyecto: String = "",
    var descripcion: String = "",
    var estado: String = "",
    var contacto: String = ""

)