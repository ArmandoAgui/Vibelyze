package com.vibelyze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.vibelyze.ui.navigation.AppNavGraph
import com.vibelyze.ui.theme.VibelyzeTheme
import com.vibelyze.ui.screens.auth.SignUpViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val signUpViewModel: SignUpViewModel by viewModels()

        setContent {
            VibelyzeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController, signUpViewModel = signUpViewModel)
                }
            }
        }
    }
}

