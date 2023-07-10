package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.ActivitySearchState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.util.Creator
import com.playlistmaker.util.Resource

class ActivitySearchVm(application: Application) : AndroidViewModel(application) {
    private val mainHandler = android.os.Handler(Looper.getMainLooper())
    private var musicTrackIsClickable = true
    private var musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()

    private var searchScreenState = MutableLiveData<ActivitySearchState>()
    val getSearchScreenState = searchScreenState as LiveData<ActivitySearchState>

    private var startPlayerApp = MutableLiveData<Boolean>()
    val getStartPlayerCommand = startPlayerApp as LiveData<Boolean>

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMsg(): LiveData<String> = errorMessage

    private val musRepo by lazy { Creator.getCreator().getMusicRepository() }


    init {
        searchScreenState.postValue(ActivitySearchState.InitialState(null))

    }

    private fun searchMusic(songName: String) {
        val musRequest = Creator.getCreator().createMusicSearchRequest(songName)
        val searchUseCase = Creator.getCreator().provideSearchMusicUseCase()

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
            saveCurrentPlayingTrack(track = trackClicked)
            this.startPlayerApp.postValue(true)

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

    private fun safeMusicHistorySearch(musicList: ArrayList<MusicTrack>) {
        musRepo.safeMusicSearchHistory(musicList)
    }

    fun saveCurrentPlayingTrack(track: MusicTrack) {
        val musTrackRepo = Creator.getCreator().getMusicTrackRepository(context = getApplication())
        musTrackRepo.setCurrentMusicTrack(track)
        startPlayerApp.postValue(true)
    }

    fun deleteMusicHistory() {
        musRepo.deleteAllMusicSearchHistory()
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
        val history = musRepo.loadMusicSearchHistory()

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

    // Фабрика для создания ViewModel с пробросом Activity в конструктор
    companion object {
        const val SEARCH_DELAY = 2000L
        const val CLICK_DELAY = 3000L

        fun getFactory(app: Application): ViewModelProvider.Factory {
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ActivitySearchVm::class.java)) {
                        return ActivitySearchVm(application = app) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
            return factory
        }
    }
}