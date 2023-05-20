package com.playlistmaker.searchhistory

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.playlistmaker.ActivitySearch
import com.playlistmaker.Logic.SearchTrackAdapter
import com.playlistmaker.Logic.Track
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.itunes.ItunesTrack

class History {
    val trackHistoryList: ArrayList<ItunesTrack> = arrayListOf()

    fun addToSearchHistory(track: ItunesTrack) {
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

        val data = Gson().fromJson(jSonHistory, Array<ItunesTrack>::class.java)
        if (data.isNullOrEmpty()) {
            // Message about empty history
        }
        else {
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