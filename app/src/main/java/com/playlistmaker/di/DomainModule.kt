package com.playlistmaker.di

import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.usecase.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.LoadLastPlayingMusicTrackUseCase
import com.playlistmaker.domain.usecase.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.MusicPlayerController
import com.playlistmaker.domain.usecase.SafeCurrentPlayingTrackUseCase
import com.playlistmaker.domain.usecase.SafeMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.domain.usecase.SettingsController
import org.koin.dsl.module

val domainModule = module {
    // UseCase для поиска музыки
    factory { SearchMusicUseCase(musicRepo = get()) }

    // UseCase для сохранения истории найденных музыкальных треков
    factory { SafeMusicSearchHistoryUseCase(musicRepository = get()) }

    // UseCase для загрузки истории найденных музыкальных треков
    factory { LoadMusicSearchHistoryUseCase(musicRepository = get()) }

    // UseCase для удаления истории найденных музыкальных треков
    factory { DeleteMusicSearchHistoryUseCase(musicRepository = get()) }

    // UseCase сохранения последнего проигранного трека
    factory { SafeCurrentPlayingTrackUseCase(musicTrackRepository = get()) }

    // UseCase загрузки последнего проигранного трека
    factory { LoadLastPlayingMusicTrackUseCase(musicTrackRepository = get()) }

    // UseCase для загрузки контроллера проигрыванием музыки
    factory<MusicPlayerController> { (externalListener: OnPlayerStateListener) ->
        MusicPlayerControllerImpl(listener = externalListener)
    }

    // UseCase для загрузки контроллера настроек приложения
    factory { SettingsController(settingsRepository = get()) }
}