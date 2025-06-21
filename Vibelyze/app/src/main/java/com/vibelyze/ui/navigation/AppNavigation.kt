package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vibelyze.ui.LoginScreen
import com.vibelyze.ui.onboarding.OnboardingScreen
import com.vibelyze.ui.onboarding.OnboardingViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    // ─── Arrancar en “login” si ya viste el onboarding ─────
    val onboardingVm: OnboardingViewModel = viewModel()
    val hasDone by onboardingVm.hasFinishedOnboarding.collectAsState(initial = false)

    NavHost(
        navController    = navController,
        startDestination = if (hasDone) "login" else "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    onboardingVm.finishOnboarding()
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen()
        }
    }
}
