package com.dam2jms.factoriafp24.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dam2jms.factoriafp24.models.ViewModelHome
import com.dam2jms.factoriafp24.states.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AñadirProyecto(navController: NavController, mvvm: ViewModelHome) {
    var context = LocalContext.current
    val uiState by mvvm.uiState.collectAsState()

    //actualizo los datos cada vez que vuelvo a la pantalla
    mvvm.leerProyectos(context) { proyectos ->
        mvvm.actualizarProyectos(proyectos)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Factoria de Proyectos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Atras")
                    }
                }
            )
        },
    ) { paddingValues ->
        añadirProyectoBody(modifier = Modifier.padding(paddingValues), navController, mvvm, uiState)
    }
}

@Composable
fun añadirProyectoBody(modifier: Modifier, navController: NavController, mvvm: ViewModelHome, uiState: UiState) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = uiState.nombreProyecto,
            onValueChange = { mvvm.onChangeAñadir(it, uiState.descripcion, uiState.estado, uiState.contacto) },
            label = { Text("Nombre del Proyecto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { mvvm.onChangeAñadir(uiState.nombreProyecto, it, uiState.estado, uiState.contacto) },
            label = { Text("Descripcion del Proyecto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = uiState.estado,
            onValueChange = { mvvm.onChangeAñadir(uiState.nombreProyecto, uiState.descripcion, it, uiState.contacto) },
            label = { Text("Estado del Proyecto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = uiState.contacto,
            onValueChange = { mvvm.onChangeAñadir(uiState.nombreProyecto, uiState.descripcion, uiState.estado, it) },
            label = { Text("Contacto del Proyecto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                val nuevoProyecto = Proyecto(nombreProyecto = uiState.nombreProyecto, descripcion = uiState.descripcion, estado = uiState.estado, contacto = uiState.contacto)
                mvvm.agregarProyecto(nuevoProyecto, context)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Añadir Proyecto")
        }
    }
}