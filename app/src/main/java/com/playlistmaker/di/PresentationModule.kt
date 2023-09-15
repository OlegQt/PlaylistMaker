package com.playlistmaker.di

import com.playlistmaker.presentation.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<FragmentSearchVm> {
        FragmentSearchVm(
            historySafeUseCase = get(),
            loadHistoryUseCase = get(),
            deleteHistoryUseCase = get(),
            safePlayingTrackUseCase = get(),
            searchUseCase = get(),
            loadFavouriteTracksIds = get()
        )
    }
    viewModel<ActivityMainVm> { ActivityMainVm() }

    viewModel<PlayerVm> {
        PlayerVm(
            addToFavoriteUseCase = get(),
            loadFavouriteUseCase = get()
        )
    }

    viewModel<FragmentSettingsVm> {
        FragmentSettingsVm(settingsController = get())
    }
    viewModel { FragmentFavouriteTracksVm() }
    viewModel { FragmentPlayListsVm() }
}