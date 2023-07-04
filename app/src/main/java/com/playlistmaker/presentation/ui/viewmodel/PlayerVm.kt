package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.domain.models.MusicTrack

class PlayerVm(private val application: Application) : AndroidViewModel(application) {

    // Переменная для хранения текущего трека
    private val currentPlayingMusTrack = MutableLiveData<MusicTrack>()
    val getCurrentMusTrack = this.currentPlayingMusTrack as LiveData<MusicTrack>

    fun loadCurrentMusicTrack() {
        val musTrackRepo = MusicTrackRepositoryImpl(application.baseContext)
        val musTrack = musTrackRepo.getCurrentMusicTrack()

        if (musTrack!=null) this.currentPlayingMusTrack.postValue(musTrack)
    }







    companion object {
        fun getFactory(app: Application): ViewModelProvider.Factory {
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerVm::class.java)) {
                        return PlayerVm(application = app) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
            return factory

        }
    }
}