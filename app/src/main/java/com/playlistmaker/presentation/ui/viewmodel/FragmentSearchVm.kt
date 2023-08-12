package com.playlistmaker.presentation.ui.viewmodel

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.domain.usecase.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SafeCurrentPlayingTrackUseCase
import com.playlistmaker.domain.usecase.SafeMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.SingleLiveEvent
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.util.Resource
import org.koin.java.KoinJavaComponent.getKoin

class FragmentSearchVm(
    private val historySafeUseCase: SafeMusicSearchHistoryUseCase,
    private val loadHistoryUseCase: LoadMusicSearchHistoryUseCase,
    private val deleteHistoryUseCase: DeleteMusicSearchHistoryUseCase,
    private val safePlayingTrackUseCase: SafeCurrentPlayingTrackUseCase,
    private val searchUseCase: SearchMusicUseCase = getKoin().get()

) : ViewModel() {
    private val mainHandler = android.os.Handler(Looper.getMainLooper())
    private var musicTrackIsClickable = true
    private var musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()

    // LiveData block
    private var searchScreenState = MutableLiveData<ActivitySearchState>()
    val getSearchScreenState = searchScreenState as LiveData<ActivitySearchState>

    private var startPlayerApp = SingleLiveEvent<MusicTrack>()
    val getStartPlayerCommand = startPlayerApp as LiveData<MusicTrack>

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMsg(): LiveData<String> = errorMessage

    init {
        searchScreenState.value = ActivitySearchState.InitialState(null)
    }

    private fun searchMusic(songName: String) {
        val musRequest = SearchRequest.MusicSearchRequest(searchParam = songName)

        // Используем UseCase DOMAIN слоя
        searchUseCase.executeSearch(musRequest) { foundMusic ->
            mainHandler.post { analiseMusicSearchResponse(foundMusic) }
        }
    }

    private fun analiseMusicSearchResponse(result: Resource<ArrayList<MusicTrack>>) {
        if (result.data.isNullOrEmpty()) {
            when (result.error) {
                ErrorList.NETWORK_TROUBLES -> {
                    searchScreenState.postValue((ActivitySearchState.InternetTroubles(null)))
                }

                ErrorList.NOTHING_FOUND -> {
                    searchScreenState.postValue((ActivitySearchState.NothingFound(null)))
                }

                else -> {}
            }
        } else {
            searchScreenState.postValue(ActivitySearchState.MusicSearchContent(result.data))
        }
    }

    fun musicTrackOnClick(trackClicked: MusicTrack) {
        if (musicTrackIsClickable) {
            // Добавляем нажатый трек в историю просмотра треков и
            // сохраняем нажатый трек, как играющий
            // и запускаем плеер
            addMusicTrackToHistorySearch(musicTrackToSafe = trackClicked)
            //saveCurrentPlayingTrack(track = trackClicked)
            this.startPlayerApp.postValue(trackClicked)

            // Блокируем доступ к нажатиям на треки на время
            mainHandler.postDelayed({ musicTrackIsClickable = true }, CLICK_DELAY)
            musicTrackIsClickable = false
        } else errorMessage.value = "Double click detected"

    }

    private fun addMusicTrackToHistorySearch(musicTrackToSafe: MusicTrack) {
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

    fun onHistoryTrackListClick(trackClicked: MusicTrack) {
        // Добавляем нажатый трек в историю просмотра треков и
        // сохраняем нажатый трек, как играющий
        // и запускаем плеер
        this.musicTrackOnClick(trackClicked)

        // Запускаем функцию отображения истории поиска с обновленным списком треков
        // Задержка специально, чтобы убрать визуальный проскок трека до загрузки медиа плеера
        mainHandler.postDelayed({
            searchScreenState.postValue(
                ActivitySearchState.HistoryMusicContent(
                    musicSearchHistoryList
                )
            )
        }, CLICK_DELAY)
    }

    private fun safeMusicHistorySearch(musicList: ArrayList<MusicTrack>) {
        historySafeUseCase.execute(musicList)
    }

    private fun saveCurrentPlayingTrack(track: MusicTrack) {
        this.safePlayingTrackUseCase.execute(track)
    }

    fun deleteMusicHistory() {
        deleteHistoryUseCase.execute()
        musicSearchHistoryList.clear()
        searchScreenState.postValue(ActivitySearchState.InitialState(null))
    }

    fun searchLineTyping(strSearch: String) {
        if (strSearch.isNotEmpty()) {
            // Переводим режим экрана в состояние загрузки
            // Реализация экрана загрузки на стороне UI в Activity
            searchScreenState.postValue(ActivitySearchState.Loading(null))

            // Перезагружаем поиск с задержкой в 2сек
            mainHandler.removeCallbacksAndMessages(null)
            mainHandler.postDelayed({ searchMusic(strSearch) }, SEARCH_DELAY)
        } else {
            mainHandler.removeCallbacksAndMessages(null)
            loadMusicHistorySearch()
        }
    }

    private fun loadMusicHistorySearch() {
        // Подгружаем историю поиска музыки
        val history = loadHistoryUseCase.execute()

        // Если история поиска не пуста запускаем изменение состояния activity
        // отображаем recycler с историей поиска треков
        // Если в истории поиска нет треков, отображаем начальное состояние экрана
        if (!history.data.isNullOrEmpty()) {
            musicSearchHistoryList = history.data
            searchScreenState.postValue(
                ActivitySearchState.HistoryMusicContent(
                    musicSearchHistoryList
                )
            )
        } else searchScreenState.postValue(ActivitySearchState.InitialState(null))

    }

    companion object {
        const val SEARCH_DELAY = 2000L
        const val CLICK_DELAY = 300L
    }
}