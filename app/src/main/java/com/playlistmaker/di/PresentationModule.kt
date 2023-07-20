package com.playlistmaker.di

import com.playlistmaker.domain.usecase.DeleteMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.LoadMusicSearchHistoryUseCase
import com.playlistmaker.domain.usecase.SafeCurrentPlayingTrackUseCase
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import com.playlistmaker.presentation.ui.viewmodel.ActivitySearchVm
import com.playlistmaker.presentation.ui.viewmodel.ActivitySettingsVm
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

val presentationModule = module {
    viewModel<ActivitySearchVm> {
        ActivitySearchVm(
            historySafeUseCase = get(),
            loadHistoryUseCase = get(),
            deleteHistoryUseCase = get(),
            safePlayingTrackUseCase = get(),
            searchUseCase = get()
        )
    }
    viewModel<ActivityMainVm> { ActivityMainVm() }
    viewModel<PlayerVm> {
        PlayerVm(
            loadLastPlayingTrackUseCase = get()
        )
    }
    viewModel<ActivitySettingsVm> {
        ActivitySettingsVm(settingsController = get())
    }
}