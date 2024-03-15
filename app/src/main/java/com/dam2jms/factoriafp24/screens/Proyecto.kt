package com.dam2jms.factoriafp24.screens

import com.google.firebase.firestore.PropertyName

data class Proyecto(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("nombreProyecto") @set:PropertyName("nombreProyecto") var nombreProyecto: String = "",
    @get:PropertyName("descripcion") @set:PropertyName("descripcion") var descripcion: String = "",
    @get:PropertyName("estado") @set:PropertyName("estado") var estado: String = "",
    @get:PropertyName("contacto") @set:PropertyName("contacto") var contacto: String = ""
)