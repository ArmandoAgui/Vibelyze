package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vibelyze.ui.screens.auth.*
import com.vibelyze.ui.screens.home.EmotionHomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login
    ) {
        composable(NavRoutes.Login) {
            LoginScreen(
                navController = navController
            )
        }

        composable(NavRoutes.SignUp) {
            SignUpScreen(
                navController = navController,
                viewModel = signUpViewModel
            )
        }

        composable(NavRoutes.ConfirmPassword) {
            ConfirmPasswordScreen(
                navController = navController,
                viewModel = signUpViewModel
            )
        }

        composable(NavRoutes.Home) {
            EmotionHomeScreen()
        }
    }
}