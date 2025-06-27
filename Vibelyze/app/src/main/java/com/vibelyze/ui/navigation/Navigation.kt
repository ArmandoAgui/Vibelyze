package com.vibelyze.ui.navigation

sealed class Screen(val route: String, val title: String){
    object Home: Screen("home", "Pantalla de Inicio")
    object Login: Screen("login", "Iniciar Sesión")
    object OnBoarding: Screen("onboarding", "Únete")
}