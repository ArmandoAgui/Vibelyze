package com.vibelyze.data
import com.vibelyze.R


data class Emotion(val label: String, val iconId: Int)

val emotions = listOf(
    Emotion("De todo", R.drawable.ic_anything_goes),
    Emotion("Feliz", R.drawable.ic_happy),
    Emotion("Aburrido", R.drawable.ic_bored),
    Emotion("Triste", R.drawable.ic_sad),
    Emotion("Emocionado", R.drawable.ic_excited)
)