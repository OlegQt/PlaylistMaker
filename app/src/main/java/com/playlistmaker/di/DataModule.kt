package com.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.playlistmaker.data.db.favourite.MusicDB
import com.playlistmaker.data.db.playlist.PlayListMapper
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.data.network.ItunesMediaSearchApi
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.FavouriteMusicRepositoryImpl
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.PlayListRepositoryImpl
import com.playlistmaker.data.repository.SettingsRepositoryImpl
import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PREFERENCES = "APP_PREFERENCES"

val dataModule = module {

    // Репозиторий списка треков (истории либо найденных)
    single<MusicRepository> {
        MusicRepositoryImpl(
            networkClient = get(),
            sharedPreferences = get(),
            gSon = get()
        )
    }

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

    single { androidContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) }


    // Репозиторий для загрузки и сохранения текущего (играющего) трека
    single<MusicTrackRepository> {
        MusicTrackRepositoryImpl(
            sharedPreferences = get(),
            gSon = get()
        )
    }

    // Репозиторий для работы с избранными треками через SQLite (Room)
    single { FavouriteMusicRepositoryImpl(db = get(), mapper = get()) }

    // Репозиторий загрузки и сохранения настроек приложения
    single<SettingsRepository> { SettingsRepositoryImpl(sharedPreferences = get()) }

    single<FavouriteMusicRepository> { FavouriteMusicRepositoryImpl(db = get(), mapper = get()) }

    single<PlayListRepository> {
        PlayListRepositoryImpl(
            db = get(),
            mapper = get(),
            trackMapper = get(),
            gSon = get()
        )
    }

    single { RetrofitNetworkClient(mediaApi = get(), context = get()) }

    single { Room.databaseBuilder(androidContext(), MusicDB::class.java, "music_app_database").build() }

    factory { MusicTrackMapper() }

    factory { PlayListMapper() }


}