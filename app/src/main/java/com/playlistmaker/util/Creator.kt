package com.playlistmaker.util

import android.content.Context
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.ScreenRepositoryImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.usecase.LoadScreenUseCase
import com.playlistmaker.domain.usecase.SafeLastScreenUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase

class Creator private constructor(){

    fun createMusicSearchRequest(strRequest:String):MusicSearchRequest{
        return MusicSearchRequest(songName = strRequest)
    }

    fun getMusicRepository():MusicRepositoryImpl{
        return MusicRepositoryImpl(networkClient = RetrofitNetworkClient())
    }
    fun provideSearchMusicUseCase():SearchMusicUseCase{
        return SearchMusicUseCase(musicRepo = getMusicRepository())
    }

    fun getMusicTrackRepository(externalContext: Context):MusicTrackRepositoryImpl{
        return MusicTrackRepositoryImpl(context = externalContext)
    }

    fun provideMusicPlayer(externalListener: OnPlayerStateListener):MusicPlayerControllerImpl{
        return MusicPlayerControllerImpl(listener = externalListener)
    }

    private fun getScreenRepository(context: Context):ScreenRepositoryImpl{
        return ScreenRepositoryImpl(context)
    }

    fun provideLoadLastScreenUseCase(externalContext: Context):LoadScreenUseCase{
        return LoadScreenUseCase(getScreenRepository(context = externalContext))
    }

    fun provideSafeLastScreenUseCase(externalContext: Context):SafeLastScreenUseCase{
        return SafeLastScreenUseCase(getScreenRepository(context = externalContext))
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