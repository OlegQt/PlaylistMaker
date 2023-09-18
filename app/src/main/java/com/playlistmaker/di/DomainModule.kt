package com.playlistmaker.di

import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.usecase.*
import com.playlistmaker.domain.usecase.dbfavourite.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavourite.DeleteMusicTrackFromFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavourite.LoadFavouriteTracksIdsUseCase
import com.playlistmaker.domain.usecase.dbfavourite.LoadFavouriteTracksUseCase
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

    // UseCase для добавления треков в базу данных избранных треков
    factory { AddMusicTrackToFavouritesUseCase(favouriteTracksRepository = get()) }

    // UseCase для загрузки треков из базы данных избранных треков
    factory { LoadFavouriteTracksUseCase(favouriteTracksRepo = get()) }

    factory { LoadFavouriteTracksIdsUseCase(favouriteTracksRepo = get()) }

    factory { DeleteMusicTrackFromFavouritesUseCase(favouriteTracksRepo = get()) }
}