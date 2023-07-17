package com.playlistmaker.di

import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import com.playlistmaker.presentation.ui.viewmodel.ActivitySearchVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<ActivitySearchVm> { ActivitySearchVm() }
    viewModel<ActivityMainVm> { ActivityMainVm() }
}