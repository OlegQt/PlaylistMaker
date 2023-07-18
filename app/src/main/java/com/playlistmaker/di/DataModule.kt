package com.playlistmaker.di

import android.content.Context
import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.SearchResponse
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.SettingsRepositoryImpl
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.domain.repository.SettingsRepository
import org.koin.dsl.module
import kotlin.math.sin


val dataModule = module {
    single<MusicRepository>{
        MusicRepositoryImpl(networkClient = get(), context = get())
    }

   single<NetworkClient<SearchRequest, SearchResponse>> {
        RetrofitNetworkClient()
    }

    single{RetrofitNetworkClient()}

    single<MusicTrackRepository>{
        MusicTrackRepositoryImpl(context = get())
    }

    single<SettingsRepository>{
        SettingsRepositoryImpl(get())
    }
}