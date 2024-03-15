package com.dam2jms.factoriafp24.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dam2jms.factoriafp24.models.ViewModelLogin
import com.dam2jms.factoriafp24.models.ViewModelHome
import com.dam2jms.factoriafp24.models.ViewModelRegister
import com.dam2jms.factoriafp24.screens.AñadirProyecto
import com.dam2jms.factoriafp24.screens.EliminarProyecto
import com.dam2jms.factoriafp24.screens.HomeScreen
import com.dam2jms.factoriafp24.screens.LoginScreen
import com.dam2jms.factoriafp24.screens.ModificarProyecto
import com.dam2jms.factoriafp24.screens.RegisterScreen

@Composable
fun appNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) { LoginScreen(navController, mvvm = ViewModelLogin()) }
        composable(route = AppScreens.RegisterScreen.route) { RegisterScreen(navController, mvvm = ViewModelRegister()) }
        composable(route = AppScreens.HomeScreen.route) { HomeScreen(navController, mvvm = ViewModelHome()) }
        composable(route = AppScreens.AñadirProyecto.route) {AñadirProyecto(navController, mvvm = ViewModelHome())}
        composable(route = AppScreens.EliminarProyecto.route) { EliminarProyecto(navController, mvvm = ViewModelHome()) }
        composable(route = AppScreens.ModificarProyecto.route) { ModificarProyecto(navController, mvvm = ViewModelHome()) }
    }
}