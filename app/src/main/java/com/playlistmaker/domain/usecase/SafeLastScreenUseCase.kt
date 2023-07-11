package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.ScreenRepository

class SafeLastScreenUseCase(private val screenRepository: ScreenRepository) {
    fun execute(safeParam:String){
        screenRepository.safeLastScreen(screenName = safeParam)
    }
}