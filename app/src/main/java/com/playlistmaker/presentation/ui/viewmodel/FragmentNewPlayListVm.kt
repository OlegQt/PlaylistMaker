package com.playlistmaker.presentation.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import com.playlistmaker.presentation.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

open class FragmentNewPlayListVm(
    private val playListController: PlayListController
) : ViewModel() {
    // Error message
    protected val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private val _btnCreateEnable = MutableLiveData<Boolean>()
    val btnCreateEnable = _btnCreateEnable as LiveData<Boolean>

    private val _selectedImage = MutableLiveData<Uri>()
    val selectedImage: LiveData<Uri> = _selectedImage

    val exitTrigger = SingleLiveEvent<Boolean>()

    private var newPlayList = PlayList()
    val playListName: String get() = newPlayList.name


    init {
        _btnCreateEnable.value = false
    }

    open fun handlePickedImage(uri: Uri) {
        // После выбора картинки, сохраняем идентификатор внутри viewModel
        _selectedImage.value = uri
    }

    fun nothingChanged(): Boolean {
        return selectedImage.value == null &&
                newPlayList.name.isBlank() &&
                newPlayList.description.isBlank()

    }

    open fun changePlayListName(newName: String) {
        // Если текстовое поле названия плейлиста пустое, скрываем кнопку создать
        _btnCreateEnable.value = newName.isNotEmpty()
        newPlayList.name = newName
    }

    open fun changeDescription(newDescription: String) {
        newPlayList.description = newDescription
    }

    open fun updatePlayListCoverLocation(file: File) {
        newPlayList.cover = file.toString()
    }

    fun savePlayListToDB() {
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            _errorMsg.value = throwable.message
        }
        // Запускаю сохранение в другом потоке
        // После завершения сохранения, дергаем триггер на выход из приложения
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            playListController.savePlaylist(newPlayList)
            _errorMsg.postValue("Плейлист $playListName создан")
            exitTrigger.postValue(true) // Закрытие фрагмента только после завершения сохранения
        }
    }
}