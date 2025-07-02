package com.vibelyze.ui.playsong

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlaySongScreen(emotion: String, viewModel: SongViewModel = viewModel()){
    val song by viewModel.song.collectAsState()

    LaunchedEffect(emotion) {
        viewModel.fetchSongByEmotion(emotion)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
            colors = listOf(Color(0xFF1B1F3B), Color(0xFF0B0F2F))
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        // Validar de la canción es nula
        if (song != null){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Emoción: ${song!!.emotion}", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "🎵 ${song!!.title}", fontSize = 24.sp, color = Color.Cyan)
                Text(text = "👤 ${song!!.artist}", fontSize = 18.sp, color = Color.LightGray)
            }
        } else {
            CircularProgressIndicator(color = Color.White)
        }
    }
}