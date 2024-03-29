package com.dam2jms.factoriafp24.screens

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dam2jms.factoriafp24.models.ViewModelHome
import com.dam2jms.factoriafp24.models.ViewModelLogin
import com.dam2jms.factoriafp24.navigation.AppScreens
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, mvvm: ViewModelHome) {
    val context = LocalContext.current
    val uiState by mvvm.uiState.collectAsState()
    val proyectos = uiState.proyectos

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

    //actualizo los datos cada vez que se vuelva a la pantalla
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
                )
            )
        },
        bottomBar = {
            if (isLoggedIn) {
                //muestro la barra de herramientas con botones si el usuario esta registrado
                BottomAppBar {
                    Button(onClick = { navController.navigate(route = AppScreens.AñadirProyecto.route) }) {
                        Text("Añadir")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { navController.navigate(route = AppScreens.EliminarProyecto.route) }) {
                        Text("Eliminar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { navController.navigate(route = AppScreens.ModificarProyecto.route) }) {
                        Text("Modificar")
                    }
                }
            } else {
                //no mostrar la barra de herramientas con botones si el usuario es un invitado
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    ) { paddingValues ->
        homeScreenBody(modifier = Modifier.padding(paddingValues), navController, mvvm, proyectos)
    }
}


@Composable
fun homeScreenBody(modifier: Modifier, navController: NavController, mvvm: ViewModelHome, proyectos: List<Proyecto>) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(proyectos) { proyecto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Nombre del Proyecto: ${proyecto.nombreProyecto}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Descripción: ${proyecto.descripcion}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Estado: ${proyecto.estado}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Contacto: ${proyecto.contacto}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
