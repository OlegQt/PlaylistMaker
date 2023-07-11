package com.playlistmaker.util

import android.content.Context
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.SettingsRepositoryImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.domain.usecase.SettingsController

class Creator private constructor(){

    fun createMusicSearchRequest(strRequest:String):MusicSearchRequest{
        return MusicSearchRequest(songName = strRequest)
    }

    fun getMusicRepository(externalContext: Context):MusicRepositoryImpl{
        return MusicRepositoryImpl(networkClient = RetrofitNetworkClient(), context = externalContext)
    }
    fun provideSearchMusicUseCase(context: Context):SearchMusicUseCase{
        return SearchMusicUseCase(musicRepo = getMusicRepository(externalContext = context))
    }

    fun getMusicTrackRepository(externalContext: Context):MusicTrackRepositoryImpl{
        return MusicTrackRepositoryImpl(context = externalContext)
    }
    fun provideMusicPlayer(externalListener: OnPlayerStateListener):MusicPlayerControllerImpl{
        return MusicPlayerControllerImpl(listener = externalListener)
    }

    fun getSettingsRepository(context: Context):SettingsRepositoryImpl{
        return SettingsRepositoryImpl(context = context)
    }
    fun provideSettingsController(externalContext: Context):SettingsController{
        return SettingsController(settingsRepository = getSettingsRepository(context = externalContext) )
    }

    override fun toString(): String {
        return "Class Creator"
    }

    companion object{
        var instance:Creator? = null

        fun getCreator():Creator{
            if(instance==null) {
                instance = Creator()

            }
            return instance!!
        }
    }
}