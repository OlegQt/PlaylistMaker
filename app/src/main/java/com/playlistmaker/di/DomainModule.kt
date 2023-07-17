package com.playlistmaker.di

import android.content.Context
import com.playlistmaker.domain.usecase.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.LoadLastPlayingMusicTrackUseCase
import com.playlistmaker.domain.usecase.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SafeCurrentPlayingTrackUseCase
import com.playlistmaker.domain.usecase.SafeMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import org.koin.dsl.module

val domainModule = module {
    // UseCase для поиска музыки
    factory<SearchMusicUseCase> {
        SearchMusicUseCase(musicRepo = get())
    }

    // UseCase для сохранения истории найденных музыкальных треков
    factory<SafeMusicSearchHistoryUseCase> {
        SafeMusicSearchHistoryUseCase(musicRepository = get())
    }

    // UseCase для загрузки истории найденных музыкальных треков
    factory<LoadMusicSearchHistoryUseCase>{
        LoadMusicSearchHistoryUseCase(musicRepository = get())
    }

    // UseCase для удаления истории найденных музыкальных треков
    factory<DeleteMusicSearchHistoryUseCase>{
        DeleteMusicSearchHistoryUseCase(musicRepository = get())
    }

    // UseCase сохранения последнего проигранного трека
    factory { SafeCurrentPlayingTrackUseCase(musicTrackRepository = get()) }

    factory { LoadLastPlayingMusicTrackUseCase(musicTrackRepository = get()) }

}
