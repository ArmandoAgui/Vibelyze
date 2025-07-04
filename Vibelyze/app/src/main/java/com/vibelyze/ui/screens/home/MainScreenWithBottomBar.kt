package com.vibelyze.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.vibelyze.ui.navigation.NavRoutes
import com.vibelyze.ui.screens.home.EmotionHomeScreen
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController
import com.vibelyze.ui.screens.profile.ProfileScreen

@Composable
fun MainScreenWithBottomBar(mainNavController: NavController) {
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomBarItem("Inicio", Icons.Default.Home, NavRoutes.Emotion),
        BottomBarItem("Playlists", Icons.Default.Favorite, NavRoutes.Playlists),
        BottomBarItem("Perfil", Icons.Default.AccountCircle, NavRoutes.Profile),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavRoutes.Emotion,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.Emotion) {
                EmotionHomeScreen()
            }
            composable(NavRoutes.Playlists) {
                Text("🎵 Pantalla de playlists")
            }
            composable(NavRoutes.Profile) {
                ProfileScreen(navController = mainNavController) // ✅ usamos el nav principal
            }
        }
    }
}


data class BottomBarItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)