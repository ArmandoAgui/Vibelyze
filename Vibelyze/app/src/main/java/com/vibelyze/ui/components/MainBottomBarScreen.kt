package com.vibelyze.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vibelyze.ui.navigation.NavRoutes

@Composable
fun MainBottomBarScreen(navController: NavController) {
    val items = listOf(
        BottomBarItem("Inicio", Icons.Default.Home, NavRoutes.Emotion),
        BottomBarItem("Playlists", Icons.Default.Favorite, NavRoutes.Playlists),
        BottomBarItem("Perfil", Icons.Default.AccountCircle, NavRoutes.Profile),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
    ) { padding ->
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            NavRoutes.Emotion -> com.vibelyze.ui.screens.home.EmotionHomeScreen()
            NavRoutes.Playlists -> Text("🎵 Aquí irán las playlists") // Reemplaza con tu pantalla real
            NavRoutes.Profile -> Text("👤 Aquí irá el perfil") // Reemplaza con tu pantalla real
        }
    }
}

data class BottomBarItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
