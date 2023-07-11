package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.ScreenRepository

class LoadScreenUseCase(private val screenRepository:ScreenRepository) {
    fun execute():String{
        return screenRepository.loadLastScreen()
    }
}