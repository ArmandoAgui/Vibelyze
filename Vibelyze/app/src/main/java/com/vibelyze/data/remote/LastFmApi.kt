package com.vibelyze.data.remote

import com.vibelyze.data.model.LastFmResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {
    @GET("?")
    suspend fun searchTracks(
        @Query("method") method: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): LastFmResponse
}
