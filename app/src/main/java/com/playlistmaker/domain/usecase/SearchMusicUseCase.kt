package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack

class SearchMusicUseCase(private val musicRepo: MusicRepository) {
    fun executeSearch(searchParams: Any): List<MusicTrack> {
        var songList:ArrayList<MusicTrack> = ArrayList()
        val searchThread = Thread {
            songList = musicRepo.searchMusic(searchParams)
        }
        searchThread.start()
        searchThread.join()

        // Пока что вернется пустой лист в случае любой ошибки
        return songList
    }
}