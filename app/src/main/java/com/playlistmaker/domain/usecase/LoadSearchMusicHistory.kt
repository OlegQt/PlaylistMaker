package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.util.Resource

class LoadSearchMusicHistory(private val musicRepo: MusicRepository) {
    fun execute(): Resource<ArrayList<MusicTrack>> {
        return musicRepo.loadMusicSearchHistory()
    }
}