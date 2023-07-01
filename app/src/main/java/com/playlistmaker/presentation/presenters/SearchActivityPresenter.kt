package com.playlistmaker.presentation.presenters

import android.content.Context
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.models.SearchActivityView

class SearchActivityPresenter(private val state: SearchActivityView,private val context: Context) {
    init {
        //state.render(ActivitySearchState.InitialState(null))

        //state.render(ActivitySearchState.NothingFound(""))
        state.render(ActivitySearchState.InternetTroubles(null))

    }

    fun searchMusic(songName: String) {
        val musRequest = MusicSearchRequest(songName)
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        val searchUseCase = SearchMusicUseCase(musRepo)

        val searchResArray = searchUseCase.executeSearch(musRequest)

        // Добавить проверку ошибок и вызвать необходимое обновление view в Activity
        state.render(ActivitySearchState.Content(searchResArray))
    }

    fun saveCurrentPlayingTrack(track: MusicTrack){
        val musTrackRepo:MusicTrackRepositoryImpl = MusicTrackRepositoryImpl(context)
        musTrackRepo.setCurrentMusicTrack(track)
    }
}