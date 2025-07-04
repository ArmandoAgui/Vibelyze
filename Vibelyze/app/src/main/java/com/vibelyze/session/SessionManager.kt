package com.vibelyze.session

object SessionManager {
    var isPremium: Boolean = false

    private var searchTimestamps = mutableListOf<Long>()

    fun canSearch(): Boolean {
        if (isPremium) return true
        val now = System.currentTimeMillis()
        searchTimestamps = searchTimestamps.filter { now - it <= 3600000 }.toMutableList()
        return searchTimestamps.size < 5
    }

    fun recordSearch() {
        if (!isPremium) {
            searchTimestamps.add(System.currentTimeMillis())
        }
    }
}
