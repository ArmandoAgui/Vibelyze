package com.vibelyze.data.remote

import com.vibelyze.data.model.lastfm.LastFmResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET("?method=tag.getTopTracks&format=json")
    suspend fun getTopTracksByTag(
        @Query("tag") tag: String,
        @Query("api_key") apiKey: String
    ): LastFmResponse
}