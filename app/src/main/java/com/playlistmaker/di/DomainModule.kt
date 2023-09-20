package com.playlistmaker.di

import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.usecase.apppreferences.SettingsController
import com.playlistmaker.domain.usecase.apppreferences.SettingsControllerImpl
import com.playlistmaker.domain.usecase.dbfavouritetracks.AddMusicTrackToFavouritesUseCaseImpl
import com.playlistmaker.domain.usecase.dbfavouritetracks.DeleteMusicTrackFromFavouritesUseCaseImpl
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.LoadFavouriteTracksIdsUseCaseImpl
import com.playlistmaker.domain.usecase.dbfavouritetracks.LoadFavouriteTracksUseCaseImpl
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.DeleteMusicTrackFromFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksIdsUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import com.playlistmaker.domain.usecase.searchhistory.DeleteMusicSearchHistoryUseCaseImpl
import com.playlistmaker.domain.usecase.searchhistory.LoadMusicSearchHistoryUseCaseImpl
import com.playlistmaker.domain.usecase.searchhistory.SafeMusicSearchHistoryUseCaseImpl
import com.playlistmaker.domain.usecase.searchhistory.interfaces.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.searchhistory.interfaces.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.searchhistory.interfaces.SafeMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.searchmusic.SearchMusicUseCase
import com.playlistmaker.domain.usecase.searchmusic.SearchMusicUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    // UseCase для поиска музыки
    factory<SearchMusicUseCase> { SearchMusicUseCaseImpl(musicRepo = get()) }

    // UseCase для сохранения истории найденных музыкальных треков
    factory<SafeMusicSearchHistoryUseCase> { SafeMusicSearchHistoryUseCaseImpl(musicRepository = get()) }

    // UseCase для загрузки истории найденных музыкальных треков
    factory<LoadMusicSearchHistoryUseCase> { LoadMusicSearchHistoryUseCaseImpl(musicRepository = get()) }

    // UseCase для удаления истории найденных музыкальных треков
    factory<DeleteMusicSearchHistoryUseCase> { DeleteMusicSearchHistoryUseCaseImpl(musicRepository = get()) }

    // UseCase для загрузки контроллера проигрыванием музыки
    factory<MusicPlayerController> { (externalListener: OnPlayerStateListener) ->
        MusicPlayerControllerImpl(listener = externalListener)
    }

    // UseCase для загрузки контроллера настроек приложения
    factory<SettingsController> { SettingsControllerImpl(settingsRepository = get()) }

    // UseCase для добавления трека в базу данных избранных треков
    factory<AddMusicTrackToFavouritesUseCase> { AddMusicTrackToFavouritesUseCaseImpl(favouriteTracksRepository = get())}

    // UseCase для загрузки треков из базы данных избранных треков
    factory<LoadFavouriteTracksUseCase> { LoadFavouriteTracksUseCaseImpl(favouriteTracksRepo = get()) }

    // UseCase для загрузки идентификаторов избранных треков
    factory<LoadFavouriteTracksIdsUseCase> { LoadFavouriteTracksIdsUseCaseImpl(favouriteTracksRepo = get()) }

    // UseCase для удаления трека из базы данных избранных треков
    factory<DeleteMusicTrackFromFavouritesUseCase> { DeleteMusicTrackFromFavouritesUseCaseImpl(favouriteTracksRepo = get()) }


}