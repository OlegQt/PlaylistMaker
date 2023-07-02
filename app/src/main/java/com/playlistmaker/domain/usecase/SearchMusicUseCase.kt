package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource
import java.util.concurrent.Executors

class SearchMusicUseCase(private val musicRepo: MusicRepository) {
    var resourceAnswer: Resource<ArrayList<MusicTrack>> = Resource.Error(0)

    fun executeSearch(searchParams: Any,consumer: MusicConsumer) {
        val searchThread = Thread {
            consumer.consume(foundMovies = musicRepo.searchMusic(searchParams))
        }
        searchThread.start()
    }

    fun interface MusicConsumer {
        fun consume(foundMovies: Resource<ArrayList<MusicTrack>>)
    }
}