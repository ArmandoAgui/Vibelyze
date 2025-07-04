package com.vibelyze.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vibelyze.data.model.lastfm.Track
import com.vibelyze.ui.theme.Purple40
import com.vibelyze.ui.theme.Purple80
import com.vibelyze.ui.theme.PurpleGrey40
import com.vibelyze.ui.theme.VibelyzeTheme
import com.vibelyze.viewmodel.EmotionMusicViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EmotionHomeScreen(viewModel: EmotionMusicViewModel = viewModel()) {
    val tracks by viewModel.tracks
    val isLoading by viewModel.isLoading
    val apiKey = "0ca85b42944c4f03c6bc396e685a3fb3" // ⬅️ REEMPLAZA con tu key real

    val emotionTags = mapOf(
        "😀" to "happy",
        "😥" to "sad",
        "😡" to "angry",
        "😭" to "cry",
        "😍" to "love",
        "🤔" to "thinking",
        "😮" to "surprise",
        "😴" to "sleep"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Cómo te sientes hoy?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Purple80,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(emotionTags.toList()) { (emoji, tag) ->
                EmotionItem(emoji, tag) {
                    println("🎯 Tag seleccionado: $tag")
                    viewModel.fetchTracksForEmotion(tag, apiKey)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Text("Cargando canción...", color = Color.White)
        } else {
            tracks.firstOrNull()?.let { track ->
                MusicPlayerCard(track) {
                    // Aquí puedes agregar lógica para guardar en Firestore
                    println("🎵 Agregado a la playlist: ${track.name} - ${track.artist.name}")
                }

            } ?: Text("No se encontró una canción", color = Color.White)
        }
    }
}
@Composable
fun MusicPlayerCard(track: Track, onAddToPlaylist: () -> Unit) {
    val imageUrl = track.image.lastOrNull()?.url ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen grande del álbum
        AsyncImage(
            model = imageUrl,
            contentDescription = "Portada del track",
            modifier = Modifier
                .size(300.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Título de la canción
        Text(
            text = track.name,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        // Nombre del artista
        Text(
            text = track.artist.name,
            color = Color(0xFFBBBBBB),
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Botón elegante para agregar
        Button(
            onClick = { onAddToPlaylist() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .height(50.dp)
                .width(220.dp)
        ) {
            Text(text = "➕ Agregar a playlist", fontSize = 16.sp)
        }
    }
}


@Composable
fun EmotionItem(emoji: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            color = Purple40,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 28.sp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 14.sp, color = Color.White)
    }
}

