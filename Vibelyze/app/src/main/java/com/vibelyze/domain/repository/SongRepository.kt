package com.vibelyze.domain.repository

import com.vibelyze.data.model.Track
import com.vibelyze.data.remote.LastFmApi
import javax.inject.Inject

class SongRepository @Inject constructor(private val api: LastFmApi) {
    suspend fun getSongbyEmotion(emotion: String): Track {
        val response = api.searchTracks(
            method = "track.search",
            track = emotion,
            apiKey = "32adcdeaf9fae34ff024912e21cde1d6",
            format = "json"
        )
        return response.results.trackmatches.track.first()
    }
}