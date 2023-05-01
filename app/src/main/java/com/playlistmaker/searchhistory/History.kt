package com.playlistmaker.searchhistory

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.playlistmaker.ActivitySearch
import com.playlistmaker.Logic.Track
import com.playlistmaker.R
import com.playlistmaker.Theme.App

class History {
    private val trackHistoryList: MutableList<Track> = mutableListOf()
    lateinit var txtHistory: TextView

    init {
        loadHistory()
    }

    fun deployExtraUi(activity: ActivitySearch) {
        txtHistory = activity.findViewById<TextView>(R.id.history)
        loadHistory()
        showAllSearchHistory()

        txtHistory.setOnLongClickListener() {
            App.instance.sharedPreferences.edit().remove(App.SEARCH_HISTORY).apply()
            true
        }

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

        showAllSearchHistory() // Show history in View

        saveHistory()  // Save history to sharedPreferences
    }

    fun setVisibility(visibility:Boolean){
        if (visibility) txtHistory.visibility = View.VISIBLE
        else txtHistory.visibility = View.GONE
    }

    private fun showAllSearchHistory() {
        val sb: StringBuilder = java.lang.StringBuilder()
        var inter = 0;
        this.trackHistoryList.forEach {
            sb.append((++inter).toString())
            sb.append(it.trackName)
            sb.append("\n")
        }
        this.txtHistory.text = sb.toString()
    }

    private fun saveHistory() {
        if (!trackHistoryList.isNullOrEmpty()) {
            val jSonHistory = Gson().toJson(trackHistoryList)
            App.instance.sharedPreferences.edit().putString(App.SEARCH_HISTORY, jSonHistory).apply()
        }
    }

    private fun loadHistory() {
        val jSonHistory = App.instance.sharedPreferences.getString(App.SEARCH_HISTORY, "")

        val data = Gson().fromJson(jSonHistory, Array<Track>::class.java)
        if (data.isNullOrEmpty()) Toast.makeText(
            App.instance.baseContext,
            "EmptyHistory",
            Toast.LENGTH_SHORT
        ).show()
        else{
            trackHistoryList.clear()
            data.forEach {
                trackHistoryList.add(it)
            }
        }
    }
}