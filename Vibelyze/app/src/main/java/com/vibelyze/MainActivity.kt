package com.vibelyze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vibelyze.ui.screens.auth.LoginScreen
import com.vibelyze.ui.screens.auth.SignUpScreen
import com.vibelyze.ui.theme.VibelyzeTheme


import com.vibelyze.ui.screens.auth.ConfirmPasswordScreen

import androidx.activity.viewModels
import com.google.firebase.FirebaseApp
import com.vibelyze.ui.screens.auth.HomeScreen
import com.vibelyze.ui.screens.auth.SignUpViewModel
import com.vibelyze.ui.screens.home.EmotionHomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            VibelyzeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EmotionHomeScreen() // 👈 Aquí lo cargas directamente
                }
            }
        }
    }
}

