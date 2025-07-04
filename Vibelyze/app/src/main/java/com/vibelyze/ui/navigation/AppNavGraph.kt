package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vibelyze.ui.screens.auth.*
import com.vibelyze.ui.screens.home.EmotionHomeScreen
import com.vibelyze.ui.components.MainBottomBarScreen
import com.vibelyze.ui.screens.home.MainScreenWithBottomBar

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

        composable(NavRoutes.Home) {
            MainScreenWithBottomBar(mainNavController = navController)
        }

        composable(NavRoutes.Emotion) {
            EmotionHomeScreen()
        }
        composable(NavRoutes.Playlists) {
            // Tu pantalla de Playlists

        }
        composable(NavRoutes.Profile) {
            // Tu pantalla de perfil

        }

    }
}


