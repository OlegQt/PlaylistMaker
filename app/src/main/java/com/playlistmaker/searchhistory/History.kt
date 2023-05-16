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

class History {
    private val trackHistoryList: ArrayList<Track> = arrayListOf()
    private lateinit var btnClearHistory: Button
    private lateinit var loutHistory: LinearLayout
    private lateinit var recyclerHistory: RecyclerView


    private val listener = object : SearchTrackAdapter.OnTrackClickListener {
        override fun onTrackClick(position: Int) {
            //
        }
    }
    private val adapterHistory = SearchTrackAdapter(trackHistoryList, listener)


    fun deployExtraUi(activity: ActivitySearch) {
        // Эта функция вызывается в OnCreate активити
        btnClearHistory = activity.findViewById(R.id.btn_clear_history)
        loutHistory = activity.findViewById(R.id.history_layout)
        recyclerHistory = activity.findViewById(R.id.history_search_recycle_view)


        val musLayOut = LinearLayoutManager(activity)
        musLayOut.orientation = RecyclerView.VERTICAL
        recyclerHistory.layoutManager = musLayOut
        recyclerHistory.adapter = adapterHistory

        btnClearHistory.setOnClickListener { clearHistory() }

        loadHistory() // Подгружаем историю поиска
    }

    fun addToSearchHistory(track: Track) {
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

    fun setVisibility(visibility: Boolean) {
        // Показываем список просмотров только если он не пуст
        if (visibility and trackHistoryList.isNotEmpty()) {
            loutHistory.visibility = View.VISIBLE
        } else loutHistory.visibility = View.GONE

    }

    fun showAllSearchHistory() {
        setVisibility(true)
        // Список просмотренных треков мог обновиться, уведомляем адаптер
        adapterHistory.notifyDataSetChanged()
    }

    private fun saveHistory() {
        if (trackHistoryList.isNotEmpty()) {
            val jSonHistory = Gson().toJson(trackHistoryList)
            App.instance.sharedPreferences.edit().putString(App.SEARCH_HISTORY, jSonHistory).apply()
        }
    }

    private fun loadHistory() {
        val jSonHistory = App.instance.sharedPreferences.getString(App.SEARCH_HISTORY, "")

        val data = Gson().fromJson(jSonHistory, Array<Track>::class.java)
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

    private fun clearHistory() {
        App.instance.sharedPreferences.edit().remove(App.SEARCH_HISTORY).apply()
        trackHistoryList.clear()
        showAllSearchHistory()
    }
}