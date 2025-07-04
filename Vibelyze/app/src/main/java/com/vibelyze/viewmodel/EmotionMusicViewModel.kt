package com.vibelyze.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibelyze.data.model.lastfm.Track
import com.vibelyze.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

class EmotionMusicViewModel : ViewModel() {
    var tracks = mutableStateOf<List<Track>>(emptyList())
    var isLoading = mutableStateOf(false)

    fun fetchTracksForEmotion(emotionTag: String, apiKey: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.getTopTracksByTag(tag = emotionTag, apiKey = apiKey)
                val allTracks = response.tracks.trackList.filter { it.image.isNotEmpty() }
                println("🎶 Total tracks recibidos: ${allTracks.size}")

                // ✅ Elige una canción aleatoria
                tracks.value = if (allTracks.isNotEmpty()) listOf(allTracks.random()) else emptyList()

            } catch (e: Exception) {
                println("❌ Error al obtener tracks: ${e.message}")
                tracks.value = emptyList()
            }
            isLoading.value = false
        }
    }


}