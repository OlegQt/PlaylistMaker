package com.playlistmaker.presentation.ui.viewmodel

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.domain.usecase.*
import com.playlistmaker.domain.usecase.dbfavourite.LoadFavouriteTracksIdsUseCase
import com.playlistmaker.presentation.SingleLiveEvent
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.util.Resource
import kotlinx.coroutines.*

class FragmentSearchVm(
    private val historySafeUseCase: SafeMusicSearchHistoryUseCase,
    private val loadHistoryUseCase: LoadMusicSearchHistoryUseCase,
    private val deleteHistoryUseCase: DeleteMusicSearchHistoryUseCase,
    private val safePlayingTrackUseCase: SafeCurrentPlayingTrackUseCase,
    private val searchUseCase: SearchMusicUseCase,
    private val loadFavouriteTracksIds: LoadFavouriteTracksIdsUseCase

) : ViewModel() {
    private val mainHandler = android.os.Handler(Looper.getMainLooper())
    private var musicTrackIsClickable = true
    private var musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()
    private var favouriteTracksIdsList = mutableListOf<Long>()

    private var searchTextEditDebounce: Job? = null

    // LiveData block
    private var searchScreenState = MutableLiveData<ActivitySearchState>()
    val getSearchScreenState = searchScreenState as LiveData<ActivitySearchState>

    private var startPlayerApp = SingleLiveEvent<MusicTrack>()
    val getStartPlayerCommand = startPlayerApp as LiveData<MusicTrack>

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMsg(): LiveData<String> = errorMessage

    init {
        searchScreenState.value = ActivitySearchState.InitialState(null)
        this.loadFavouriteTracksIdList()
    }

    private fun searchMusic(songName: String) {
        val musRequest = SearchRequest.MusicSearchRequest(searchParam = songName)

        // Уловитель ошибок
        val errorCoroutine = CoroutineExceptionHandler { coroutineContext, throwable ->
            // Показываем ошибку пользователю
            errorMessage.postValue(throwable.message)
        }

        // Запуск поиска музыкальных треков
        viewModelScope.launch(errorCoroutine) {
            searchUseCase.executeSearchViaCoroutines(musRequest).collect {
                analiseMusicSearchResponse(it)
            }
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

    private fun clickDebounce(): Boolean {
        return if (musicTrackIsClickable) {
            // Блокируем доступ к нажатиям на треки на время
            musicTrackIsClickable = false

            // Запускаем отложенное действие по разрешению нажатий
            viewModelScope.launch {
                delay(CLICK_DELAY_MLS)
                musicTrackIsClickable = true
            }
            true
        } else {
            errorMessage.value = "Double click detected"
            false
        }
    }

    fun musicTrackOnClick(trackClicked: MusicTrack) {
        if (clickDebounce()) {
            // Добавляем нажатый трек в историю просмотра треков
            // и запускаем плеер

            addMusicTrackToHistorySearch(musicTrackToSafe = trackClicked)

            // Проверка наличия данного трека в базе избранных треков
            // Если трек уже есть в базе, изменить значение isFavourite на true
            this.startPlayerApp.postValue(trackClicked.apply {
                isFavourite = trackIsFavourite(this) })
        }

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
        }, REORDER_HISTORY_MLS)
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
            searchTextEditDebounce?.cancel()
            searchTextEditDebounce = viewModelScope.launch {
                delay(SEARCH_DELAY_MLS)
                searchMusic(strSearch)
            }
        } else {
            searchTextEditDebounce?.cancel()
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

    fun loadFavouriteTracksIdList() {
        // Загружаем в глобальную переменную список, состоящий из идентификаторов (ID)
        // Всех треков, находящихся в базе данных избранных треков

        viewModelScope.launch(Dispatchers.IO) {
            loadFavouriteTracksIds.execute().collect() { it ->
                favouriteTracksIdsList.clear()
                favouriteTracksIdsList.addAll(it)
            }
            withContext(Dispatchers.Main) { errorMessage.value = favouriteTracksIdsList.toString() }
        }
    }

    private fun trackIsFavourite(someMusicTrack: MusicTrack):Boolean{

        // Сверяет id трека со списком id избранных треков
        // Возвращает true если трек уже добавлен в избранное и false в обратном случае
        return favouriteTracksIdsList.contains(someMusicTrack.trackId)
    }

    companion object {
        const val SEARCH_DELAY_MLS = 2000L
        const val CLICK_DELAY_MLS = 300L
        const val REORDER_HISTORY_MLS = 1000L

        const val LOG_TAG = "LOG_TAG"
    }
}