package com.playlistmaker.domain.usecase.searchmusic

import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface SearchMusicUseCase {
    fun executeSearch(searchParams: SearchRequest, consumer: MusicConsumer)
    fun executeSearchViaCoroutines(searchParams: SearchRequest): Flow<Resource<ArrayList<MusicTrack>>>
}