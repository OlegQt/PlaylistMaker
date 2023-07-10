package com.playlistmaker.util

import android.content.Context
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.domain.usecase.SearchMusicUseCase

class Creator private constructor(){
    var count = 0

    fun increase(){
        this.count++
    }

    fun createMusicSearchRequest(strRequest:String):MusicSearchRequest{
        return MusicSearchRequest(songName = strRequest)
    }

    fun getMusicRepository():MusicRepositoryImpl{
        return MusicRepositoryImpl(networkClient = RetrofitNetworkClient())
    }
    fun provideSearchMusicUseCase():SearchMusicUseCase{
        return SearchMusicUseCase(musicRepo = getMusicRepository())
    }

    fun getMusicTrackRepository(context: Context):MusicTrackRepositoryImpl{
        return MusicTrackRepositoryImpl(context = context)
    }


    override fun toString(): String {
        return "Class Creator ($count)"
    }

    companion object{
        var instance:Creator? = null

        fun getCreator():Creator{
            if(instance==null) {
                instance = Creator()
            }
            else{
                instance?.increase()
            }
            return instance!!
        }
    }
}