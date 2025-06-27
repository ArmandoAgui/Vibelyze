package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vibelyze.ui.LoginScreen
import com.vibelyze.ui.mainscreen.MainScreen
import com.vibelyze.ui.onboarding.OnboardingScreen

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
