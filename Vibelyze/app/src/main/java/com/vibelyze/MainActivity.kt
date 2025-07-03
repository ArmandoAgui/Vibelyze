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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val signUpViewModel: SignUpViewModel by viewModels()

        setContent {
            VibelyzeTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("signup") {
                            SignUpScreen(navController = navController, viewModel = signUpViewModel)
                        }
                        composable("confirmPasswordScreen") {
                            ConfirmPasswordScreen(navController = navController, viewModel = signUpViewModel)
                        }
                        composable("homeScreen") {
                            HomeScreen(navController)
                        }
                    }

                }
            }
        }
    }
}
