package com.playlistmaker.presentation.presenters

import android.content.Context
import android.os.Looper
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.models.SearchActivityView

class SearchActivityPresenter(private val state: SearchActivityView, private val context: Context) {
    private val mainHandler = android.os.Handler(Looper.getMainLooper())
    private var musicTrackIsClickable = true
    private var musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()

    init {
        state.render(ActivitySearchState.InitialState(null))
    }

    private fun loadMusicHistorySearch(){
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())

        // Подгружаем историю поиска музыки
        val history = musRepo.loadMusicSearchHistory()

        // Если история поиска не пуста запускаем изменение состояния activity
        // отображаем recycler с историей поиска треков
        // Если в истории поиска нет треков, отображаем начальное состояние экрана
        if (!history.data.isNullOrEmpty()){
            musicSearchHistoryList = history.data
            state.render(ActivitySearchState.HistoryMusicContent(musicSearchHistoryList))
        }
        else state.render(ActivitySearchState.InitialState(null))

    }

    private fun safeMusicHistorySearch(musicList: ArrayList<MusicTrack>){
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        musRepo.safeMusicSearchHistory(musicList)
    }

    private fun addMusicTrackToHistorySearch(musicTrackToSafe: MusicTrack){
        // Search if track is already exists, returns track index in list
        // Return -1 if no such element was found.
        val res = musicSearchHistoryList.indexOf(musicTrackToSafe)
        if (res != -1) {
            // Ниже реализовал смещение трека на первую позицию
            val temporaryTrack = musicSearchHistoryList[res]
            musicSearchHistoryList.removeAt(res)
            musicSearchHistoryList.add(0, temporaryTrack)
        } else {
            // Добавляем track в начало
            // Если треков больше 10 удаляем заключительный
            musicSearchHistoryList.add(0, musicTrackToSafe)
            if (musicSearchHistoryList.size > 10) musicSearchHistoryList.removeLast()
        }
        this.safeMusicHistorySearch(musicSearchHistoryList)
    }

    fun deleteMusicHistory(){
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        musicSearchHistoryList.clear()
        musRepo.deleteAllMusicSearchHistory()
        state.render(ActivitySearchState.InitialState(null))
    }

    fun searchLineTyping(strSearch: String) {
        if(strSearch.isNotEmpty()) {
            // Переводим режим экрана в состояние загрузки
            // Реализация экрана загрузки на стороне UI в Activity
            state.render(ActivitySearchState.Loading(null))

            // Перезагружаем поиск с задержкой в 2сек
            mainHandler.removeCallbacksAndMessages(null)
            mainHandler.postDelayed({ searchMusic(strSearch) }, 2000)
        }
        else{
            mainHandler.removeCallbacksAndMessages(null)
            loadMusicHistorySearch()
        }
    }

    private fun searchMusic(songName: String) {
        val musRequest = MusicSearchRequest(songName)
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        val searchUseCase = SearchMusicUseCase(musRepo)

        // Используем UseCase DOMAIN слоя
        val searchResult = searchUseCase.executeSearch(musRequest)

        if (searchResult.data.isNullOrEmpty()) {
            when (searchResult.errorCode) {
                -1 -> state.render((ActivitySearchState.InternetTroubles(null)))
                -500 -> state.render((ActivitySearchState.NothingFound(null)))
            }
        } else {
            state.render(ActivitySearchState.MusicSearchContent(searchResult.data))
        }
    }

    fun musicTrackOnClick(trackClicked:MusicTrack){
        if (musicTrackIsClickable) {
            // Добавляем нажатый трек в историю просмотра треков и
            // сохраняем нажатый трек, как играющий
            // и запускаем плеер
            addMusicTrackToHistorySearch(musicTrackToSafe = trackClicked)
            saveCurrentPlayingTrack(track = trackClicked)
            state.startPlayerActivity()

            // Блокируем доступ к нажатиям на треки на время
            mainHandler.postDelayed({musicTrackIsClickable=true},300)
            musicTrackIsClickable = false
        }
        else{
            state.showAlertDialog("${trackClicked.trackName} Double click detected")
        }
    }

    fun saveCurrentPlayingTrack(track: MusicTrack) {
        val musTrackRepo: MusicTrackRepositoryImpl = MusicTrackRepositoryImpl(context)
        musTrackRepo.setCurrentMusicTrack(track)
    }
}