package com.vibelyze.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vibelyze.data.model.lastfm.Track
import com.vibelyze.session.SessionManager
import com.vibelyze.ui.theme.Purple40
import com.vibelyze.ui.theme.Purple80
import com.vibelyze.ui.theme.PurpleGrey40
import com.vibelyze.viewmodel.EmotionMusicViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vibelyze.ui.screens.playlist.AddToPlaylistDialog
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.vibelyze.R

@Composable
fun EmotionHomeScreen(viewModel: EmotionMusicViewModel = viewModel()) {
    val context = LocalContext.current
    val tracks by viewModel.tracks
    val isLoading by viewModel.isLoading
    val apiKey = "0ca85b42944c4f03c6bc396e685a3fb3"

    var showLimitDialog by remember { mutableStateOf(false) }
    var selectedTrack by remember { mutableStateOf<Track?>(null) }

    val emotionTags = mapOf(
        "😀" to "happy", "😥" to "sad", "😡" to "angry", "😭" to "cry",
        "😍" to "love", "🤔" to "thinking", "😮" to "surprise", "😴" to "sleep"
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
                    val isPremium = SessionManager.isPremium
                    val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

                    if (isPremium || uid == null) {
                        viewModel.fetchTracksForEmotion(tag, apiKey)
                    } else {
                        checkAndHandleLimit(uid, tag, apiKey, viewModel) {
                            showLimitDialog = true
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Text("Cargando canción...", color = Color.White)
        } else {
            tracks.firstOrNull()?.let { track ->
                MusicPlayerCard(track) {
                    selectedTrack = track
                }
            } ?: Text("No se encontró una canción", color = Color.White)
        }

        if (showLimitDialog) {
            AlertDialog(
                onDismissRequest = { showLimitDialog = false },
                confirmButton = {
                    TextButton(onClick = { showLimitDialog = false }) {
                        Text("Aceptar", color = Color.White)
                    }
                },
                title = {
                    Text("Límite alcanzado", color = Color.White)
                },
                text = {
                    Text(
                        "Has alcanzado tu límite de 5 búsquedas por hora. ⏱️\n\nIntenta más tarde o hazte Premium 💎 para búsquedas ilimitadas.",
                        color = Color.White
                    )
                },
                containerColor = Color(0xFF1F1F1F),
                titleContentColor = Color.White,
                textContentColor = Color.White
            )
        }

        selectedTrack?.let { track ->
            AddToPlaylistDialog(track = track, onDismiss = { selectedTrack = null })
        }
    }
}

fun checkAndHandleLimit(
    uid: String,
    tag: String,
    apiKey: String,
    viewModel: EmotionMusicViewModel,
    onLimitReached: () -> Unit
) {
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
    val userDoc = db.collection("users").document(uid)
    val now = System.currentTimeMillis()

    userDoc.get().addOnSuccessListener { snapshot ->
        val lastTimestamp = snapshot.getLong("lastSearchTimestamp") ?: 0L
        val searchCount = snapshot.getLong("searchCount")?.toInt() ?: 0
        val elapsed = now - lastTimestamp

        if (elapsed > 60 * 60 * 1000) {
            userDoc.update(
                mapOf(
                    "lastSearchTimestamp" to now,
                    "searchCount" to 1
                )
            )
            viewModel.fetchTracksForEmotion(tag, apiKey)
        } else if (searchCount < 5) {
            userDoc.update(
                mapOf(
                    "searchCount" to searchCount + 1
                )
            )
            viewModel.fetchTracksForEmotion(tag, apiKey)
        } else {
            onLimitReached()
        }
    }.addOnFailureListener {
        onLimitReached()
    }
}

@Composable
fun MusicPlayerCard(track: Track, onAddToPlaylist: () -> Unit) {
    val imageUrl = track.image.lastOrNull()?.url ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = "Portada del track",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = track.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    text = track.artist.name,
                    color = Color(0xFFBBBBBB),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onAddToPlaylist() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .height(45.dp)
                        .width(200.dp)
                ) {
                    Text(text = "➕ Agregar a playlist", fontSize = 14.sp)
                }
            }
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
