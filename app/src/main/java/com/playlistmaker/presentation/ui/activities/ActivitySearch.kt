package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.logic.SearchTrackAdapter
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.databinding.ActivitySearchBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.ui.viewmodel.ActivitySearchVm

class ActivitySearch : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var vm: ActivitySearchVm


    // Адаптеры для отображения найденных треков и истории просмотра треков
    private lateinit var musicAdapter: SearchTrackAdapter
    private lateinit var musicSearchHistoryAdapter: SearchTrackAdapter

    // List of tracks
    private val musicList: ArrayList<MusicTrack> = ArrayList()
    private val musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()


    // Функция для перехода на экран плеера
    private fun startPlayerActivity(flag: Boolean) {
        if (flag) {
            finish()
            startActivity(Intent(App.instance, ActivityPlayer::class.java))
        }
    }

    private fun clsButtonVisibility() {
        val visibility = binding.txtSearch.text.isNotEmpty()
        binding.clsSearch.visibility = when (visibility) {
            true -> View.VISIBLE
            false -> View.GONE
        }

    }

    // Функции для изменения состояний экрана
    private fun stateNothingFound() {
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE

        binding.imgStub.setImageResource(R.drawable.nothing_found) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.nothing_found)
    }

    private fun stateInitial() {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE
        //showAlertDialog("InitialState")
    }

    private fun stateInternetTroubles() {
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.btnReload.visibility = View.VISIBLE

        binding.imgStub.setImageResource(R.drawable.connection_troubles) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.connection_troubles)
            .plus("\n")
            .plus(getString(R.string.loading_fail))

    }

    private fun stateLoading() {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.progressLoading.visibility = View.VISIBLE
        binding.searchRecycleView.visibility = View.GONE
    }

    // Отображение истории поиска музыкальных композиций
    private fun stateShowMusicSearchHistory(musicHistory: ArrayList<MusicTrack>) {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.VISIBLE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE

        this.musicSearchHistoryList.clear()
        this.musicSearchHistoryList.addAll(musicHistory)
        this.musicSearchHistoryAdapter.notifyDataSetChanged()

    }

    // Отображение найденного списка музыкальных композиций
    private fun updateMusicList(music: ArrayList<MusicTrack>) {
        this.musicList.clear()
        this.musicList.addAll(music)
        this.musicAdapter.notifyDataSetChanged()

        this.stateInitial()
        binding.searchRecycleView.visibility = View.VISIBLE
    }

    ////////////////////////////////////////////////////////
    // Блок переопределённых функций
    ////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        App.instance.saveCurrentScreen(Screen.SEARCH) // Сохраняем данные о переходе на главный экран приложения

        val factory = ActivitySearchVm.getFactory(this.application)
        vm = ViewModelProvider(this, factory = factory)[ActivitySearchVm::class.java]

        vm.getSearchScreenState.observe(this) { this.render(it) }

        vm.getStartPlayerCommand.observe(this) { startPlayerActivity(it) }


        // Инициализация адаптера с описанием слушателя
        musicAdapter =
            SearchTrackAdapter(musicList) {
                vm.musicTrackOnClick(musicList[it])
            }
        binding.searchRecycleView.adapter = musicAdapter
        binding.searchRecycleView.layoutManager =
            LinearLayoutManager(binding.searchRecycleView.context)

        musicSearchHistoryAdapter = SearchTrackAdapter(musicSearchHistoryList) {
            vm.saveCurrentPlayingTrack(musicSearchHistoryList[it])
            //this.startPlayerActivity()
        }
        binding.historySearchRecycleView.adapter = musicSearchHistoryAdapter
        binding.historySearchRecycleView.layoutManager =
            LinearLayoutManager(binding.searchRecycleView.context)

        binding.txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                vm.searchLineTyping(str.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.btnClearHistory.setOnClickListener { vm.deleteMusicHistory() }
        binding.btnReload.setOnClickListener { vm.searchLineTyping(binding.txtSearch.toString()) }
        binding.clsSearch.setOnClickListener { binding.txtSearch.text.clear() }
        binding.btnBack.setOnClickListener {
            App.instance.saveCurrentScreen(Screen.MAIN) // Сохраняем экран
            finish()
        }

        binding.txtSearch.setText("")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTxt", binding.txtSearch.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.binding.txtSearch.setText(savedInstanceState.getString("searchTxt").toString())
    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }

    fun showAlertDialog(msg: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(msg)
            .setTitle("Dialog")
            .setNeutralButton("OK", null)
            .show()
    }

    private fun render(state: ActivitySearchState) {
        when (state) {
            is ActivitySearchState.NothingFound -> stateNothingFound()
            is ActivitySearchState.MusicSearchContent -> updateMusicList(state.music)
            is ActivitySearchState.InitialState -> stateInitial()
            is ActivitySearchState.InternetTroubles -> stateInternetTroubles()
            is ActivitySearchState.Loading -> stateLoading()
            is ActivitySearchState.HistoryMusicContent -> stateShowMusicSearchHistory(state.music)
            else -> {}
        }
        // Проверка состояния кнопки очистки поисковой строки
        // происходит при любом State
        clsButtonVisibility()
    }
}