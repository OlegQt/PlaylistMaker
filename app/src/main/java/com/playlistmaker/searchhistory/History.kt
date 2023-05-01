package com.playlistmaker.searchhistory

import android.widget.TextView
import android.widget.Toast
import com.playlistmaker.ActivitySearch
import com.playlistmaker.Logic.Track
import com.playlistmaker.R
import com.playlistmaker.Theme.App

class History {
    val trackHistoryList:MutableSet<Track> = mutableSetOf()
    lateinit var txtHistory:TextView

    fun deployExtraUi(activity:ActivitySearch){
        txtHistory = activity.findViewById<TextView>(R.id.history)
    }

    fun addToSearchHistory(track: Track){
        if(!trackHistoryList.add(track)) Toast.makeText(App.instance.baseContext,
        "Already in history",Toast.LENGTH_SHORT).show()
        showAllSearchHistory()
    }

    fun showAllSearchHistory(){
        val sb:StringBuilder = java.lang.StringBuilder()
        this.trackHistoryList.forEach {
            sb.append(it.trackName)
        }
        this.txtHistory.text = sb.toString()
    }
}