package com.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.playlistmaker.data.network.ItunesMediaSearchApi
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.SettingsRepositoryImpl
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.domain.repository.SettingsRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PREFERENCES = "APP_PREFERENCES"

val dataModule = module {

    // Репозиторий списка треков (истории либо найденных)
    single<MusicRepository> { MusicRepositoryImpl(networkClient = get(), context = get()) }

    single<ItunesMediaSearchApi> {
        val baseUrl = "https://itunes.apple.com"

        // retrofit initialisation will come with class member initialisation
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesMediaSearchApi::class.java)
    }

    factory { Gson() }

    single { (context: Context) ->
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    single { RetrofitNetworkClient() }

    // Репозиторий для загрузки и сохранения текущего (играющего) трека
    single<MusicTrackRepository> {
        MusicTrackRepositoryImpl(context = get())
    }

    // Репозиторий загрузки и сохранения настроек приложения
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}