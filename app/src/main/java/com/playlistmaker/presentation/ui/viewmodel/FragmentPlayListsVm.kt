package com.playlistmaker.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentPlayListsVm(
    private val playListController: PlayListController
) : ViewModel() {

    // LiveData для состояния экрана фрагмента
    // FragmentPlaylistsState.NothingFound - Показывать заглушку
    // FragmentPlaylistsState.Content - Показывать плейлисты
    private val _playlistState =
        MutableStateFlow<FragmentPlaylistsState>(FragmentPlaylistsState.NothingFound(null))
    val playlistState = _playlistState as StateFlow<FragmentPlaylistsState>

    // LiveData для отображения ошибок
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    init {
        Log.e("LOG","INIT inside FragmentPlayListsVm")
        loadPlaylists()
    }

    private fun loadPlaylists() {
        // TODO: Здесь прописать загрузку избранных треков
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            _errorMsg.value = throwable.message
        }

        // Запуск отслеживания состояния базы данных плейлистов
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            playListController.loadAllPlayLists().collect {
                withContext(Dispatchers.Main) {
                    // Если не пустой список, то грузим
                    if (it.isNotEmpty()) _playlistState.emit(FragmentPlaylistsState.Content(it))
                    else _playlistState.emit(FragmentPlaylistsState.NothingFound(null))
                }
            }
        }
    }

    fun clearPlayListBD() {
        viewModelScope.launch {
            playListController.clearBD()
            _errorMsg.postValue("DB deleted successfully")
        }
    }
}