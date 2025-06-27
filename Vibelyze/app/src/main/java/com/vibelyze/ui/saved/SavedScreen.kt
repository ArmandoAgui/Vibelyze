package com.vibelyze.ui.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibelyze.ui.playlist.PlaylistItem

@Composable
fun SavedScreen(
    savedPlaylists: List<PlaylistItem>,
    onTabNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1B1F3B), Color(0xFF0B0F2F))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val tabs = listOf("Escuchadas", "Guardados", "Salir")
            val selectedTab = 1 // Guardados

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF5C7CFA)
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = {
                            when (title) {
                                "Escuchadas" -> onTabNavigate("playlists")
                                "Salir" -> onTabNavigate("login")
                                else -> {}
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == idx) Color.White else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Playlists Guardadas",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(savedPlaylists, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E335A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(text = item.title, color = Color.White, fontSize = 18.sp)
                            Text(text = item.author, color = Color(0xFFADB5BD), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
