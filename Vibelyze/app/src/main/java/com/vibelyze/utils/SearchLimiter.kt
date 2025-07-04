package com.vibelyze.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock

object SearchLimiter {
    private const val PREF_NAME = "search_prefs"
    private const val KEY_LAST_SEARCH_TIME = "last_search_time"
    private const val KEY_SEARCH_COUNT = "search_count"

    private const val MAX_SEARCHES = 5
    private const val TIME_WINDOW_MS = 60 * 60 * 1000 // 1 hora

    fun canSearch(context: Context, isPremium: Boolean): Boolean {
        if (isPremium) return true

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastTime = prefs.getLong(KEY_LAST_SEARCH_TIME, 0)
        val count = prefs.getInt(KEY_SEARCH_COUNT, 0)

        val now = System.currentTimeMillis()

        return if (now - lastTime > TIME_WINDOW_MS) {
            resetSearchCount(prefs)
            true
        } else {
            count < MAX_SEARCHES
        }
    }

    fun recordSearch(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val lastTime = prefs.getLong(KEY_LAST_SEARCH_TIME, 0)
        val count = prefs.getInt(KEY_SEARCH_COUNT, 0)

        if (now - lastTime > TIME_WINDOW_MS) {
            prefs.edit()
                .putLong(KEY_LAST_SEARCH_TIME, now)
                .putInt(KEY_SEARCH_COUNT, 1)
                .apply()
        } else {
            prefs.edit()
                .putInt(KEY_SEARCH_COUNT, count + 1)
                .apply()
        }
    }

    private fun resetSearchCount(prefs: SharedPreferences) {
        prefs.edit()
            .putLong(KEY_LAST_SEARCH_TIME, System.currentTimeMillis())
            .putInt(KEY_SEARCH_COUNT, 0)
            .apply()
    }
}
