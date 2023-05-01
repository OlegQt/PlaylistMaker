package com.playlistmaker.searchhistory

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.playlistmaker.ActivitySearch
import com.playlistmaker.Logic.Track
import com.playlistmaker.R
import com.playlistmaker.Theme.App

class History {
    val trackHistoryList: MutableList<Track> = mutableListOf()
    lateinit var txtHistory: TextView

    fun deployExtraUi(activity: ActivitySearch) {
        txtHistory = activity.findViewById<TextView>(R.id.history)
    }

    fun addToSearchHistory(track: Track) {
        // Search if track is already exists, returns track index in list
        // Return -1 if no such element was found.
        var res = trackHistoryList.indexOf(track)

        // If track is new fo historyList (res == null)
        if (res != -1) {
            // Toast.makeText(App.instance.baseContext, "Already in history ", Toast.LENGTH_SHORT).show()
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

        // Show history in View
        showAllSearchHistory()
    }

    private fun showAllSearchHistory() {
        this.txtHistory.visibility = View.VISIBLE
        val sb: StringBuilder = java.lang.StringBuilder()
        var iter = 0;
        this.trackHistoryList.forEach {
            sb.append((iter++).toString())
            sb.append(it.trackName)
            sb.append("\n")
        }
        this.txtHistory.text = sb.toString()
    }


}