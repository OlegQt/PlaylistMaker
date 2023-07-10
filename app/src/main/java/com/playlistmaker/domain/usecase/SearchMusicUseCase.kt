package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource
import java.lang.Exception

class SearchMusicUseCase(private val musicRepo: MusicRepository) {

    fun executeSearch(searchParams: Any,consumer: MusicConsumer) {
         val searchThread = Thread({
            try {
                val foundMusic = musicRepo.searchMusic(searchParams)
                consumer.consume(foundMusic)
            } catch (e: Exception) {
                // Обработка ошибки при отсутствии интернета
                consumer.consume(Resource.Error(ErrorList.NETWORK_TROUBLES))
            }
        }, "searchMusic")
        searchThread.start()
    }

    fun interface MusicConsumer {
        fun consume(foundMovies: Resource<ArrayList<MusicTrack>>)
    }
}