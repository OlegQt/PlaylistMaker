package com.playlistmaker.searchhistory

import com.google.gson.Gson
import com.playlistmaker.Theme.App
import com.playlistmaker.domain.models.MusicTrack

class History {
    val trackHistoryList: ArrayList<MusicTrack> = arrayListOf()

    fun addToSearchHistory(track: MusicTrack) {
        // Search if track is already exists, returns track index in list
        // Return -1 if no such element was found.
        val res = trackHistoryList.indexOf(track)
        if (res != -1) {
            // Ниже реализовал смещение трека на первую позицию
            val temporaryTrack = trackHistoryList[res]
            trackHistoryList.removeAt(res)
            trackHistoryList.add(0, temporaryTrack)
        } else {
            // Добавляем трэки в начало
            // Если треков больше 10 удаляем заключительный
            trackHistoryList.add(0, track)
            if (trackHistoryList.size > 10) trackHistoryList.removeLast()
        }

        saveHistory()  // Save history to sharedPreferences
    }

    private fun saveHistory() {
        if (trackHistoryList.isNotEmpty()) {
            val jSonHistory = Gson().toJson(trackHistoryList)
            App.instance.sharedPreferences.edit().putString(App.SEARCH_HISTORY, jSonHistory).apply()
        }
    }

    fun loadHistory() {
        val jSonHistory = App.instance.sharedPreferences.getString(App.SEARCH_HISTORY, "")

        val data = Gson().fromJson(jSonHistory, Array<MusicTrack>::class.java)
        if (data.isNullOrEmpty()) {
            // Message about empty history
        } else {
            trackHistoryList.clear()
            data.forEach {
                trackHistoryList.add(it)
            }
        }
    }

    fun clearHistory() {
        App.instance.sharedPreferences.edit().remove(App.SEARCH_HISTORY).apply()
        trackHistoryList.clear()
    }
}