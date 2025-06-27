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
import com.vibelyze.ui.playlist.PlaylistScreen
import com.vibelyze.ui.playlist.PlaylistViewModel
import com.vibelyze.ui.saved.SavedScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val onboardingVm: OnboardingViewModel = viewModel()
    val hasDone by onboardingVm.hasFinishedOnboarding.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = if (hasDone) "login" else "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(onFinish = {
                onboardingVm.finishOnboarding()
                navController.navigate("login")
            })
        }

        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("playlists")
            })
        }

        composable("playlists") {
            val vm: PlaylistViewModel = viewModel()
            val items by vm.playlists.collectAsState(initial = emptyList())

            PlaylistScreen(
                playlists = items,
                onPlaylistClick = { /* Navegar a detalle en el futuro */ },
                onTabNavigate = { destination ->
                    navController.navigate(destination)
                }
            )
        }

        composable("saved") {
            val vm: PlaylistViewModel = viewModel()
            val playlists by vm.playlists.collectAsState()
            SavedScreen(
                savedPlaylists = playlists,
                onTabNavigate = { navController.navigate(it) }
            )
        }

    }
}
