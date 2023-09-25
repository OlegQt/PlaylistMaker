package com.playlistmaker.di

import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentFavouriteTracksVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListsVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentSearchVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentSettingsVm
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<FragmentSearchVm> {
        FragmentSearchVm(
            historySafeUseCase = get(),
            loadHistoryUseCase = get(),
            deleteHistoryUseCase = get(),
            searchUseCase = get(),
            loadFavouriteTracksIds = get()
        )
    }
    viewModel<ActivityMainVm> { ActivityMainVm() }

    viewModel<PlayerVm> {
        PlayerVm(
            addToFavoriteUseCase = get(),
            loadFavouriteUseCase = get(),
            deleteFavouriteUseCase = get()
        )
    }

    viewModel<FragmentSettingsVm> { FragmentSettingsVm(settingsController = get()) }

    viewModel { FragmentFavouriteTracksVm(loadFavouriteTracks = get()) }

    viewModel { FragmentPlayListsVm() }
}