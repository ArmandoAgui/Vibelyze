package com.vibelyze.ui.playlist

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import java.util.Locale

data class PlaylistItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val author: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlists: List<PlaylistItem>,
    onPlaylistClick: (PlaylistItem) -> Unit,
    onTabNavigate: (String) -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }
    val duration = 240f

    LaunchedEffect(isPlaying) {
        while (isPlaying && currentPosition < duration) {
            delay(1000L)
            currentPosition += 1f
        }
        if (currentPosition >= duration) isPlaying = false
    }

    fun Float.formatTime(): String {
        val m = (this / 60).toInt()
        val s = (this % 60).toInt()
        return String.format(Locale.getDefault(), "%02d:%02d", m, s)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B1F3B), Color(0xFF0B0F2F))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val tabs = listOf("Escuchadas", "Guardados", "Salir")
            var selectedTab by remember { mutableIntStateOf(0) }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF5C7CFA)
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = {
                            selectedTab = idx
                            when (title) {
                                "Guardados" -> onTabNavigate("saved")
                                "Salir"     -> onTabNavigate("login")
                                // "Escuchadas" no navega, solo permanece
                            }
                        },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == idx) Color.White else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(
                "Playlists",
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF82AAFF),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    ),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(playlists, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .clickable { onPlaylistClick(item) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E335A))
                    ) {
                        Column {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                item.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                "by ${item.author}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFADB5BD),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2E335A), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(currentPosition.formatTime(), color = Color(0xFFCED4DA))
                    Slider(
                        value = currentPosition / duration,
                        onValueChange = { new -> currentPosition = new * duration },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color(0xFF5C7CFA),
                            inactiveTrackColor = Color.Gray
                        )
                    )
                    Text(duration.formatTime(), color = Color(0xFFCED4DA))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentPosition = (currentPosition - 10f).coerceAtLeast(0f)
                    }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", tint = Color.White)
                    }
                    IconButton(onClick = {
                        isPlaying = false
                        currentPosition = 0f
                    }) {
                        Icon(Icons.Default.Stop, contentDescription = "Detener", tint = Color.White)
                    }
                    IconButton(onClick = {
                        isPlaying = !isPlaying
                    }) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        currentPosition = (currentPosition + 10f).coerceAtMost(duration)
                    }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", tint = Color.White)
                    }
                    IconButton(onClick = { isSaved = !isSaved }) {
                        Icon(
                            if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) Color(0xFFFF6B6B) else Color.White
                        )
                    }
                }
            }
        }
    }
}
