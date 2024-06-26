package com.playlistmaker.di

import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentFavouriteTracksVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentMusicPlayerVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentNewPlayListVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListEditorVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListViewerVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListsVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentSearchVm
import com.playlistmaker.presentation.ui.viewmodel.FragmentSettingsVm
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

    viewModel { FragmentSettingsVm(settingsController = get()) }

    viewModel { FragmentFavouriteTracksVm(loadFavouriteTracks = get()) }

    viewModel { FragmentPlayListsVm(playListController = get()) }

    viewModel { FragmentNewPlayListVm(playListController = get()) }

    viewModel {
        FragmentMusicPlayerVm(
            addToFavoriteUseCase = get(),
            loadFavouriteUseCase = get(),
            deleteFavouriteUseCase = get(),
            playListController = get()
        )
    }

    viewModel{FragmentPlayListViewerVm(playListController = get())}

    viewModel{FragmentPlayListEditorVm(playListController = get())}

}