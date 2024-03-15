package com.dam2jms.factoriafp24.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dam2jms.factoriafp24.R
import com.dam2jms.factoriafp24.models.ViewModelRegister
import com.dam2jms.factoriafp24.navigation.AppScreens
import com.dam2jms.factoriafp24.states.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, mvvm: ViewModelRegister) {

    val uiState by mvvm.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Factoria de proyectos") },
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
        }
    ) { paddingValues ->
        registerScreenBody(modifier = Modifier.padding(paddingValues), mvvm, navController, uiState)
    }
}

@Composable
fun registerScreenBody(modifier: Modifier, mvvm: ViewModelRegister, navController: NavController, uiState: UiState) {

    var mostrarAlertDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_factoria),
            contentDescription = "",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = uiState.user,
            onValueChange = { mvvm.onChange(it, uiState.password, uiState.repeat_password) },
            label = { Text(text = "Usuario") },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { mvvm.onChange(uiState.user, it, uiState.repeat_password) },
            label = { Text(text = "Password") },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = uiState.repeat_password,
            onValueChange = { mvvm.onChange(uiState.user, uiState.password, it) },
            label = { Text(text = "Password") },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                mvvm.registrarUsuario(context)
                navController.navigate(route = AppScreens.LoginScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "REGISTRAR")
        }
    }
}