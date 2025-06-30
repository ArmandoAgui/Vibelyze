package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vibelyze.ui.LoginScreen
import com.vibelyze.ui.mainscreen.MainScreen
import com.vibelyze.ui.onboarding.OnboardingScreen
import com.vibelyze.ui.onboarding.OnboardingViewModel
import com.vibelyze.ui.playlist.PlaylistScreen
import com.vibelyze.ui.playlist.PlaylistViewModel
import com.vibelyze.ui.saved.SavedScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route){
            MainScreen(navController)
        }
        composable(Screen.OnBoarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen()
        }
    }
}
