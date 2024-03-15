package com.dam2jms.factoriafp24.navigation

sealed class AppScreens (val route: String){
    object LoginScreen: AppScreens(route = "login_screen")
    object RegisterScreen: AppScreens(route = "register_screen")
    object HomeScreen: AppScreens(route = "home_screen")
    object AñadirProyecto: AppScreens(route = "añadir_proyecto")
    object EliminarProyecto: AppScreens(route = "eliminar_proyecto")
    object ModificarProyecto: AppScreens(route = "modificar_proyecto")

}
