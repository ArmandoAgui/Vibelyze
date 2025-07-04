package com.vibelyze.data.model.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmResponse(
    @SerializedName("tracks") val tracks: TrackWrapper
)

data class TrackWrapper(
    @SerializedName("track") val trackList: List<Track>
)

data class Track(
    val name: String,
    val artist: Artist,
    val image: List<Image>
)

data class Artist(
    val name: String
)

data class Image(
    @SerializedName("#text") val url: String,
    val size: String
)