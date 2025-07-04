package com.vibelyze.ui.screens.playlist

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.vibelyze.ui.navigation.NavRoutes
import com.vibelyze.ui.theme.PurpleGrey40
import com.vibelyze.ui.theme.Purple80
import com.vibelyze.ui.theme.Purple40

data class Playlist(
    val id: String,
    val name: String
)

@Composable
fun PlaylistScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }

    // 🔁 Escuchar playlists
    LaunchedEffect(userId) {
        if (userId != null) {
            firestore.collection("users").document(userId)
                .collection("playlists")
                .addSnapshotListener { snapshot, _ ->
                    playlists = snapshot?.documents?.mapNotNull {
                        val name = it.getString("name")
                        val id = it.id
                        if (name != null) Playlist(id, name) else null
                    } ?: emptyList()
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Tus Playlists",
                fontSize = 24.sp,
                color = Purple80,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (playlists.isEmpty()) {
                Text("Aún no tienes playlists", color = Color.White)
            } else {
                LazyColumn {
                    items(playlists) { playlist -> // ✅ Esto es correcto
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    val encodedName = java.net.URLEncoder.encode(playlist.name, "UTF-8").replace("+", "%20")

                                    Log.d("DEBUG_NAV", "Navigating to: playlistDetail/${playlist.id}/$encodedName")

                                    navController.navigate(
                                        NavRoutes.PlaylistDetail
                                            .replace("{playlistId}", playlist.id)
                                            .replace("{playlistName}", encodedName)
                                    )

                                }
                            ,
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
                        ) {
                            Text(
                                text = playlist.name, // ✅ Aquí ahora sí es String
                                color = Color.White,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }


            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Purple40,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (newPlaylistName.isNotBlank() && userId != null) {
                            val playlistData = mapOf("name" to newPlaylistName)
                            firestore.collection("users")
                                .document(userId)
                                .collection("playlists")
                                .add(playlistData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Playlist creada 🎉", Toast.LENGTH_SHORT).show()
                                }
                            newPlaylistName = ""
                            showDialog = false
                        }
                    }) {
                        Text("Crear", color = Purple80)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        newPlaylistName = ""
                        showDialog = false
                    }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                title = { Text("Nueva Playlist", color = Purple80) },
                text = {
                    OutlinedTextField(
                        value = newPlaylistName,
                        onValueChange = { newPlaylistName = it },
                        label = { Text("Nombre de la playlist", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF2C2C2C),
                            unfocusedContainerColor = Color(0xFF2C2C2C),
                            focusedIndicatorColor = Purple80,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color.White
                        )
                    )
                },
                containerColor = Color(0xFF1F1F1F)
            )
        }
    }
}

