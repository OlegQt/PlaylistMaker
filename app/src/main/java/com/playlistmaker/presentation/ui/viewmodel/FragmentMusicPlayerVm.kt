package com.playlistmaker.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.DeleteMusicTrackFromFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TRACK_DURATION_UPDATE_mills = 300L

class FragmentMusicPlayerVm(
    private val musicalPlayer: MusicPlayerController,
    private val addToFavoriteUseCase: AddMusicTrackToFavouritesUseCase,
    private val loadFavouriteUseCase: LoadFavouriteTracksUseCase,
    private val deleteFavouriteUseCase: DeleteMusicTrackFromFavouritesUseCase,
    private val playListController: PlayListController
) : ViewModel() {
    // Состояние плеера
    private var _playerState = MutableLiveData<PlayerState>()
    val playerState = _playerState as LiveData<PlayerState>

    private val _playingTime = MutableLiveData<Long>()
    val playingTime = _playingTime as LiveData<Long>

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private val _currentPlayingMusTrack = MutableLiveData<MusicTrack>()
    val currentMusTrack = _currentPlayingMusTrack as LiveData<MusicTrack>

    // LiveData для состояния экрана фрагмента
    // FragmentPlaylistsState.NothingFound - Показывать заглушку
    // FragmentPlaylistsState.Content - Показывать плейлисты
    private val _playlistState =
        MutableStateFlow<FragmentPlaylistsState>(FragmentPlaylistsState.NothingFound(null))
    val playlistState = _playlistState as StateFlow<FragmentPlaylistsState>

    private var trackPlayingTimerListener: Job? = null

    init {

        // Запуск отслеживания БД плейлистов
        updateListOfPlaylistFromDB()
    }

    fun loadCurrentMusicTrack(trackToPlay: MusicTrack) {
        musicalPlayer.setMusicPlayerStateListener { _playerState.postValue(it) }
        musicalPlayer.preparePlayer(musTrackUrl = trackToPlay.previewUrl)
        _currentPlayingMusTrack.value = trackToPlay
    }

    fun pushPlayPauseButton() {
        Log.e("LOG", "PUSH BUTTON PLAY ${_playerState.value}")
        when (_playerState.value) {
            PlayerState.STATE_PLAYING -> playPauseMusic(false)
            PlayerState.STATE_PAUSED -> playPauseMusic(true)
            PlayerState.STATE_PREPARED -> playPauseMusic(true)
            PlayerState.STATE_COMPLETE -> playPauseMusic(true)
            else -> _errorMsg.value="UncorrectedState"
        }
    }

    fun playPauseMusic(isPlaying: Boolean) {
        if (isPlaying) musicalPlayer.playMusic()
        else musicalPlayer.pauseMusic()
    }

    fun stopTrackPlayingTimer() {
        trackPlayingTimerListener?.cancel()
    }

    fun startTrackPlayingTimer() {
        trackPlayingTimerListener = viewModelScope.launch {
            while (_playerState.value == PlayerState.STATE_PLAYING) {
                delay(TRACK_DURATION_UPDATE_mills)
                updatePlayingTime()
            }
        }
    }

    private fun updatePlayingTime() {
        _playingTime.value = musicalPlayer.getCurrentPos().toLong()
    }

    fun turnOffPlayer() {
        trackPlayingTimerListener?.cancel()
        trackPlayingTimerListener = null

        musicalPlayer.turnOffPlayer()

        // В случае вызова функции onPause фрагмента при данном state действия с
        // плеером не будут производиться
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    fun pushAddToFavButton() {
        _currentPlayingMusTrack.value?.let {
            if (it.isFavourite) deleteTrackFromFavourite(it)
            else addTrackToFavourite(it)

            _currentPlayingMusTrack.value = it.copy(isFavourite = !it.isFavourite)
        }
    }

    private fun addTrackToFavourite(musicTrack: MusicTrack) {
        // Добавляем трек в базу сразу при старте плеера временно
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            addToFavoriteUseCase.execute(musicTrack)
        }
    }

    private fun deleteTrackFromFavourite(musicTrack: MusicTrack) {
        // Добавляем трек в базу сразу при старте плеера временно
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            deleteFavouriteUseCase.execute(musicTrack)
        }
    }

    fun showAllFavouriteTracks(): Boolean {
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler) {
            val result = loadFavouriteUseCase.execute()
            result.collect {
                val strBld = StringBuilder()
                it.forEach {
                    strBld.append("${it.trackName}   ${it.trackId} \n")
                }
                _errorMsg.postValue(strBld.toString())
            }
        }
        return true
    }

    fun onPlayListClick(playList: PlayList){
        if (playList.trackList.isNotEmpty()) {
            val data: Array<Long> = Gson().fromJson(playList.trackList, Array<Long>::class.java)
            if (!data.contains(currentMusTrack.value?.trackId)) {
                val idCurrent = currentMusTrack.value?.trackId
                idCurrent?.let {
                    val newArray = data.plus(idCurrent.toLong())
                    val newListJson = Gson().toJson(newArray)
                    viewModelScope.launch {
                        playListController.updatePlayList(playList.copy(trackList = newListJson, quantity = newArray.size))
                    }
                }
            }
            else{
                _errorMsg.value="Трек уже есть ${currentMusTrack.value?.trackName}"
            }
        }
        else{
            val array = arrayOf(currentMusTrack.value?.trackId)
            val newListJson = Gson().toJson(array)
            viewModelScope.launch {
                playListController.updatePlayList(playList.copy(trackList = newListJson, quantity = array.size))
            }
        }

    }

    private fun updateListOfPlaylistFromDB() {
        viewModelScope.launch {
            playListController.loadAllPlayLists().collect {
                _playlistState.value = FragmentPlaylistsState.Content(it)
            }
        }
    }

}