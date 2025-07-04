package com.vibelyze.ui.screens.playlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vibelyze.ui.theme.PurpleGrey40
import com.vibelyze.ui.theme.Purple80

@Composable
fun PlaylistDetailScreen(playlistId: String,
                         playlistName: String,
                         navController: NavController
) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var tracks by remember { mutableStateOf<List<TrackItem>>(emptyList()) }

    // 🔁 Escuchar cambios en las canciones de la playlist
    LaunchedEffect(playlistId) {
        if (userId != null) {
            firestore.collection("users").document(userId)
                .collection("playlists").document(playlistId)
                .collection("tracks")
                .addSnapshotListener { snapshot, _ ->
                    tracks = snapshot?.documents?.mapNotNull {
                        val id = it.id
                        val name = it.getString("name")
                        val artist = it.getString("artist")
                        if (name != null && artist != null) TrackItem(id, name, artist) else null
                    } ?: emptyList()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(16.dp)
    ) {
        Text(
            text = "🎧 Playlist: $playlistName",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Purple80,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (tracks.isEmpty()) {
            Text("Esta playlist está vacía", color = Color.White)
        } else {
            LazyColumn {
                items(tracks) { track ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(track.name, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(track.artist, color = Color(0xFFBBBBBB))
                            }
                            IconButton(onClick = {
                                if (userId != null) {
                                    firestore.collection("users").document(userId)
                                        .collection("playlists").document(playlistId)
                                        .collection("tracks").document(track.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Canción eliminada", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ✅ Clase auxiliar para manejar los datos y su ID
data class TrackItem(
    val id: String,
    val name: String,
    val artist: String
)
