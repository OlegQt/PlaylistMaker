package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource
import java.lang.Exception

class SearchMusicUseCase(private val musicRepo: MusicRepository) {
    var resourceAnswer: Resource<ArrayList<MusicTrack>> = Resource.Error(0)

    fun executeSearch(searchParams: Any,consumer: MusicConsumer) {
         val searchThread = Thread({
            try {
                val foundMovies = musicRepo.searchMusic(searchParams)
                consumer.consume(foundMovies)
            } catch (e: Exception) {
                // Обработка ошибки при отсутствии интернета
                consumer.consume(Resource.Error(-1))
            }
        }, "searchMusic")
        searchThread.start()
    }

    fun interface MusicConsumer {
        fun consume(foundMovies: Resource<ArrayList<MusicTrack>>)
    }
}