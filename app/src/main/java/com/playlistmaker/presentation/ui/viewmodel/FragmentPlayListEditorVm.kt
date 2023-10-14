package com.playlistmaker.presentation.ui.viewmodel

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

open class FragmentPlayListEditorVm(
    private val playListController: PlayListController
) : FragmentNewPlayListVm(playListController) {

    private val _playListOpened = MutableLiveData<PlayList?>()
    val playListOpened = _playListOpened as LiveData<PlayList?>

    val color = MutableLiveData<Boolean>()

    private var changedPlayList = PlayList()

    fun loadPlaylistById(id: Long) {
        viewModelScope.launch {
            playListController.loadPlayListById(id).collect {
                _playListOpened.value = it
                changedPlayList = it.copy()
            }
        }
    }

    private fun isPlaylistDataChanged(): Boolean {
        _playListOpened.value?.let {
            return it == changedPlayList
        }
        return false
    }

    override fun changePlayListName(newName: String) {
        super.changePlayListName(newName)
        changedPlayList.name = newName
        color.value = isPlaylistDataChanged()
    }

    override fun changeDescription(newDescription: String) {
        super.changeDescription(newDescription)
        changedPlayList.description = newDescription
        color.value = isPlaylistDataChanged()
    }

    override fun handlePickedImage(uri: Uri) {
        super.handlePickedImage(uri)
        // Данная функция вызывается при замене изображения в области обложки плейлиста
        // На данном этапе не происходит переноса изображения во внутреннюю память приложения
        changedPlayList.cover = uri.toString()
    }

    override fun updatePlayListCoverLocation(file: File) {
        // Данная функция вызывается только при попытке сохранения изменений плейлиста
        // Функция копирует изображение во внутренне хранилище приложения и обновляет URI в плейлисте
        changedPlayList.cover = file.toString()
    }

    fun updatePlayListInfo(){
        viewModelScope.launch {
            playListController.updatePlayList(changedPlayList)
            exitTrigger.postValue(true)
        }
    }
}