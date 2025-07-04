package com.vibelyze.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import com.google.firebase.firestore.FirebaseFirestore

object SearchLimiter {
    private const val MAX_SEARCHES = 5
    private const val LIMIT_DURATION_MS = 60 * 60 * 1000 // 1 hora

    fun canSearch(
        userId: String,
        onResult: (Boolean) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val userDoc = db.collection("users").document(userId)

        userDoc.get().addOnSuccessListener { document ->
            val lastTimestamp = document.getLong("lastSearchTimestamp") ?: 0L
            val searchCount = document.getLong("searchCount")?.toInt() ?: 0
            val now = System.currentTimeMillis()

            val elapsed = now - lastTimestamp

            if (elapsed > LIMIT_DURATION_MS) {
                onResult(true)
                // Reiniciar contador si ya pasó la hora
                userDoc.update(
                    mapOf(
                        "lastSearchTimestamp" to now,
                        "searchCount" to 1
                    )
                )
            } else {
                onResult(searchCount < MAX_SEARCHES)
                if (searchCount < MAX_SEARCHES) {
                    userDoc.update(
                        "searchCount", searchCount + 1
                    )
                }
            }
        }.addOnFailureListener {
            onResult(true) // Permitir en caso de fallo
        }
    }
}

