package com.playlistmaker.domain.usecase.searchmusic

import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class SearchMusicUseCaseImpl(private val musicRepo: MusicRepository):SearchMusicUseCase {

    override fun executeSearch(searchParams: SearchRequest, consumer: MusicConsumer) {
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

    override fun executeSearchViaCoroutines(searchParams: SearchRequest):Flow<Resource<ArrayList<MusicTrack>>>{
        return musicRepo.searchMusicViaCoroutines(searchParams)
    }
}