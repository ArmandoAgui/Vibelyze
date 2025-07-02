package com.vibelyze.ui.playsong

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibelyze.data.Song
import com.vibelyze.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(private val repository: SongRepository) : ViewModel() {
    private val _song = MutableStateFlow<Song?>(null)
    val song: StateFlow<Song?> = _song

    fun fetchSongByEmotion(emotion: String) {
        viewModelScope.launch {
            val track = repository.getSongbyEmotion(emotion)
            _song.value = Song(emotion, track.name, track.artist)
        }
    }
}