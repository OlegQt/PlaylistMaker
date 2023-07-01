package com.playlistmaker

import com.playlistmaker.data.network.MusicRepositoryImpl
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.domain.api.MusicInteractor
import com.playlistmaker.domain.api.MusicRepository
import com.playlistmaker.domain.impl.MusicInteractorImpl

object Creator {
    private fun getMusicRepository():MusicRepository{
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }



}