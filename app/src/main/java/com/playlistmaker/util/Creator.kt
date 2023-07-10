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
        return MusicSearchRequest(strRequest)
    }

    fun getMusicRepository():MusicRepositoryImpl{
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideSearchMusicUseCase():SearchMusicUseCase{
        return SearchMusicUseCase(getMusicRepository())
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