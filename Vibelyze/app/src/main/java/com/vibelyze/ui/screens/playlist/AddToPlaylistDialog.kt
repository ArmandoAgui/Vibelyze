package com.vibelyze.ui.screens.playlist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vibelyze.data.model.lastfm.Track
import com.vibelyze.ui.theme.Purple80

// Importa tu clase Playlist si está en otro archivo
import com.vibelyze.ui.screens.playlist.Playlist

@Composable
fun AddToPlaylistDialog(
    track: Track,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid)
                .collection("playlists")
                .get()
                .addOnSuccessListener { result ->
                    playlists = result.documents.mapNotNull {
                        val id = it.id
                        val name = it.getString("name")
                        if (name != null) Playlist(id, name) else null
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error cargando playlists", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Agregar a playlist", color = Color.White) },
        text = {
            when {
                isLoading -> Text("Cargando playlists...", color = Color.White)
                playlists.isEmpty() -> Text("No tienes playlists aún.", color = Color.White)
                else -> {
                    LazyColumn {
                        items(playlists) { playlist ->
                            Text(
                                text = "🎵 ${playlist.name}",
                                color = Purple80,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        saveTrackToPlaylist(uid!!, playlist.id, track) {
                                            Toast.makeText(
                                                context,
                                                "Canción agregada a ${playlist.name} 🎉",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            onDismiss()
                                        }
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFF1F1F1F),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}

fun saveTrackToPlaylist(
    uid: String,
    playlistId: String,
    track: Track,
    onSuccess: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val playlistRef = db.collection("users")
        .document(uid)
        .collection("playlists")
        .document(playlistId)
        .collection("tracks")

    playlistRef
        .whereEqualTo("name", track.name)
        .whereEqualTo("artist", track.artist.name)
        .get()
        .addOnSuccessListener { result ->
            if (result.isEmpty) {
                // ✅ No existe aún → lo agregamos
                val trackData = mapOf(
                    "name" to track.name,
                    "artist" to track.artist.name,
                    "imageUrl" to (track.image.lastOrNull()?.url ?: "")
                )
                playlistRef.add(trackData)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { it.printStackTrace() }
            } else {
                // ⚠️ Ya existe → mensaje opcional
                Toast.makeText(
                    com.google.firebase.FirebaseApp.getInstance().applicationContext,
                    "La canción ya está en esta playlist.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

