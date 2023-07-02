package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource
import java.io.IOException

class SearchMusicUseCase(private val musicRepo: MusicRepository) {
    var resourceAnswer: Resource<ArrayList<MusicTrack>> = Resource.Error(0)

    fun executeSearch(searchParams: Any): Resource<ArrayList<MusicTrack>> {
        val searchThread = Thread {
            resourceAnswer = musicRepo.searchMusic(searchParams)
        }
        searchThread.start()
        searchThread.join()

        // Возвращает класс обертку с данными и сообщением ошибки
        return resourceAnswer
    }
}