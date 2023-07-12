package com.playlistmaker.util

import android.content.Context
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.data.repository.SettingsRepositoryImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.domain.repository.SettingsRepository
import com.playlistmaker.domain.usecase.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.LoadLastPlayingMusicTrackUseCase
import com.playlistmaker.domain.usecase.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SafeCurrentPlayingTrackUseCase
import com.playlistmaker.domain.usecase.SafeMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.domain.usecase.SettingsController

class Creator private constructor() {

    fun createMusicSearchRequest(strRequest: String): MusicSearchRequest {
        return MusicSearchRequest(songName = strRequest)
    }

    // Репозиторий списка треков (истории либо найденных)
    private fun getMusicRepository(externalContext: Context): MusicRepository {
        return MusicRepositoryImpl(
            networkClient = RetrofitNetworkClient(),
            context = externalContext
        )
    }

    // UseCase для поиска музыки
    fun provideSearchMusicUseCase(context: Context): SearchMusicUseCase {
        return SearchMusicUseCase(musicRepo = getMusicRepository(externalContext = context))
    }

    // UseCase для сохранения истории найденных музыкальных треков
    fun provideSafeMusicSearchHistory(context: Context): SafeMusicSearchHistoryUseCase {
        return SafeMusicSearchHistoryUseCase(getMusicRepository(externalContext = context))
    }

    // UseCase для загрузки истории найденных музыкальных треков
    fun provideLoadMusicSearchHistory(context: Context): LoadMusicSearchHistoryUseCase {
        return LoadMusicSearchHistoryUseCase(getMusicRepository(externalContext = context))
    }

    // UseCase для удаления истории найденных музыкальных треков
    fun provideDeleteMusicSearchHistory(context: Context): DeleteMusicSearchHistoryUseCase {
        return DeleteMusicSearchHistoryUseCase(getMusicRepository(externalContext = context))
    }


    /////////////////////////////////////////////////////////////////////////////////
    // Далее UseCase для работы с конкретным музыкальным треком
    /////////////////////////////////////////////////////////////////////////////////

    private fun getMusicTrackRepository(externalContext: Context): MusicTrackRepository {
        return MusicTrackRepositoryImpl(context = externalContext)
    }

    // UseCase загрузки последнего проигранного трека
    fun provideLoadLastPlayingTrackUseCase(context: Context):LoadLastPlayingMusicTrackUseCase{
        return LoadLastPlayingMusicTrackUseCase(getMusicTrackRepository(externalContext = context))
    }

    // UseCase сохранения последнего проигранного трека
    fun provideSafePlayingTrackUseCase(context: Context):SafeCurrentPlayingTrackUseCase{
        return SafeCurrentPlayingTrackUseCase(getMusicTrackRepository(externalContext = context))
    }

    fun provideMusicPlayer(externalListener: OnPlayerStateListener): MusicPlayerControllerImpl {
        return MusicPlayerControllerImpl(listener = externalListener)
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Далее UseCase для работы настройками приложения (тема день/ночь)
    /////////////////////////////////////////////////////////////////////////////////

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context = context)
    }

    fun provideSettingsController(externalContext: Context): SettingsController {
        return SettingsController(getSettingsRepository(context = externalContext))
    }

    override fun toString(): String {
        return "Class Creator"
    }

    companion object {
        var instance: Creator? = null

        fun getCreator(): Creator {
            if (instance == null) {
                instance = Creator()

            }
            return instance!!
        }
    }
}