package com.vibelyze.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la carga de playlists.
 * Actualmente usa datos de ejemplo, pero puede conectarse a un repositorio real.
 */
class PlaylistViewModel : ViewModel() {
    // Flujo interno mutable con la lista de playlists
    private val _playlists = MutableStateFlow<List<PlaylistItem>>(emptyList())
    // Exponemos el flujo como solo lectura
    val playlists: StateFlow<List<PlaylistItem>> get() = _playlists

    init {
        // Carga inicial de datos
        loadPlaylists()
    }

    /**
     * Simula una llamada remota para obtener las playlists.
     * Reemplaza esta implementación por la integración con tu API.
     */
    private fun loadPlaylists() {
        viewModelScope.launch {
            // Pequeña demora para simular tiempo de red
            delay(600)
            // Datos de ejemplo
            _playlists.value = listOf(
                PlaylistItem(
                    id = "1",
                    title = "Chill Music",
                    author = "Alejandra",
                    imageUrl = "https://via.placeholder.com/300.png"
                ),
                PlaylistItem(
                    id = "2",
                    title = "Dance Party",
                    author = "Yumaaxs",
                    imageUrl = "https://via.placeholder.com/300.png"
                ),
                PlaylistItem(
                    id = "3",
                    title = "Motivation",
                    author = "Yumaaxs",
                    imageUrl = "https://via.placeholder.com/300.png"
                ),
                PlaylistItem(
                    id = "4",
                    title = "Happy Vibes",
                    author = "Alejandra",
                    imageUrl = "https://via.placeholder.com/300.png"
                ),
                PlaylistItem(
                    id = "5",
                    title = "Epic Beats",
                    author = "Yumaaxs",
                    imageUrl = "https://via.placeholder.com/300.png"
                )
            )
        }
    }
}
