package com.vibelyze.ui.navigation

import com.vibelyze.data.Emotion

sealed class Screen(val route: String, val title: String){
    object Home: Screen("home", "Pantalla de Inicio")
    object Login: Screen("login", "Iniciar Sesión")
    object OnBoarding: Screen("onboarding", "Únete")
    object Song: Screen("song/{emotion}", "Canción") {
        fun createRoute(emotion: Emotion) =  "song/$emotion"
    }
}