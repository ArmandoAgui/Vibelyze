package com.vibelyze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vibelyze.ui.screens.auth.*
import com.vibelyze.ui.screens.home.EmotionHomeScreen
import com.vibelyze.ui.components.MainBottomBarScreen
import com.vibelyze.ui.screens.home.MainScreenWithBottomBar
import com.vibelyze.ui.screens.playlist.PlaylistDetailScreen

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

        composable(
            route = "playlistDetail/{playlistId}/{playlistName}",
            arguments = listOf(
                navArgument("playlistId") { type = NavType.StringType },
                navArgument("playlistName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: ""
            val playlistName = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("playlistName") ?: "", "UTF-8")

            PlaylistDetailScreen(
                playlistId = playlistId,
                playlistName = playlistName,
                navController = navController // si tu pantalla lo requiere
            )
        }


    }
}


