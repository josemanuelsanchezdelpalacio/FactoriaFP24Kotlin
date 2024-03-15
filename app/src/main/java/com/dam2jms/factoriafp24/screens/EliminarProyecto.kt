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
fun EliminarProyecto(navController: NavController, mvvm: ViewModelHome) {
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
        eliminarProyectoBody(modifier = Modifier.padding(paddingValues), navController, mvvm, uiState)
    }
}

@Composable
fun eliminarProyectoBody(modifier: Modifier, navController: NavController, mvvm: ViewModelHome, uiState: UiState) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = uiState.id,
            onValueChange = { mvvm.onChangeEliminar(it) },
            label = { Text("ID del Proyecto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                mvvm.eliminarProyecto(uiState.id, context) // Pasar el ID del proyecto
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Eliminar Proyecto")
        }
    }
}