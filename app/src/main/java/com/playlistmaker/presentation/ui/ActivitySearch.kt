package com.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.Logic.SearchTrackAdapter
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.databinding.ActivitySearchBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.presenters.SearchActivityPresenter
import com.playlistmaker.presentation.models.SearchActivityView

class ActivitySearch : AppCompatActivity(), SearchActivityView {
    private lateinit var binding: ActivitySearchBinding
    lateinit var presenter: SearchActivityPresenter

    // Адаптеры для отображения найденных треков и истории просмотра треков
    private lateinit var musicAdapter: SearchTrackAdapter
    private lateinit var musicSearchHistoryAdapter: SearchTrackAdapter

    // List of tracks
    private val musicList:ArrayList<MusicTrack> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable {
        presenter.searchMusic(binding.txtSearch.text.toString())
    }



    // Функция для перехода на экран плеера
    private fun startPlayerActivity() {startActivity(Intent(App.instance, ActivityPlayer::class.java))}

    // Функции для изменения состояний экрана
    private fun stateNothingFound() {
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE

        binding.imgStub.setImageResource(R.drawable.nothing_found) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.nothing_found)
    }
    private fun stateInitial(){
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
    }
    private fun stateInternetTroubles(){
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.btnReload.visibility = View.VISIBLE

        binding.imgStub.setImageResource(R.drawable.connection_troubles) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.connection_troubles)
            .plus("\n")
            .plus(getString(R.string.loading_fail))

    }

    // Замена треков на вновь найденные
    private fun updateMusicList(music: ArrayList<MusicTrack>){
        this.musicList.clear()
        this.musicList.addAll(music)
        this.musicAdapter.notifyDataSetChanged()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = SearchActivityPresenter(state = this, context = binding.root.context)


        // Инициализация адаптера с описанием слушателя
        musicAdapter = SearchTrackAdapter(this.musicList
        ) {
            presenter.saveCurrentPlayingTrack(musicList[it])
            this.startPlayerActivity() }
        binding.searchRecycleView.adapter = musicAdapter
        binding.searchRecycleView.layoutManager = LinearLayoutManager(binding.searchRecycleView.context)

        // CHECK
        //presenter.searchMusic("sting")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTxt", binding.txtSearch.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //this.strSearch = savedInstanceState.getString("searchTxt").toString()
        //binding.txtSearch.setText(this.strSearch)
    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }

    override fun showAlertDialog(msg: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("")
            .setMessage(msg)
            .setPositiveButton("Done", null)
            .show()
    }

    override fun render(state: ActivitySearchState) {
        when (state) {
            is ActivitySearchState.NothingFound -> stateNothingFound()
            is ActivitySearchState.Content -> updateMusicList(state.music)
            is ActivitySearchState.InitialState -> stateInitial()
            is ActivitySearchState.InternetTroubles -> stateInternetTroubles()
            else -> {}
        }
    }
}