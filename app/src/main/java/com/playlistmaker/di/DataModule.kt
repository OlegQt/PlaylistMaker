package com.playlistmaker.di

import android.content.Context
import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.SearchResponse
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.domain.repository.MusicRepository
import org.koin.dsl.module


val dataModule = module {
    single<MusicRepository>{
        MusicRepositoryImpl(networkClient = get(), context = get())
    }

    single<NetworkClient<SearchRequest, SearchResponse>> {
        RetrofitNetworkClient()
    }
}