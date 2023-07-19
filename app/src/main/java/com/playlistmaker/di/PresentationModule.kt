package com.playlistmaker.di

import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import com.playlistmaker.presentation.ui.viewmodel.ActivitySearchVm
import com.playlistmaker.presentation.ui.viewmodel.ActivitySettingsVm
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<ActivitySearchVm> { ActivitySearchVm() }
    viewModel<ActivityMainVm> { ActivityMainVm() }
    viewModel<PlayerVm>{PlayerVm()}
    viewModel<ActivitySettingsVm>{ActivitySettingsVm()}
}