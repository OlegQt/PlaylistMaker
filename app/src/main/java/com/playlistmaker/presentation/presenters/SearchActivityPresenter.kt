package com.playlistmaker.presentation.presenters

import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.models.SearchActivityView

class SearchActivityPresenter(private val state:SearchActivityView) {
    fun searchMusic(songName:String){
        val musRequest = MusicSearchRequest(songName)
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        val searchUseCase = SearchMusicUseCase(musRepo)

        val searchResArray = searchUseCase.executeSearch(musRequest)

        // Добавить проверку ошибок и вызвать необходимое обновление view в Activity
        //state.showAlertDialog(searchResArray.toString())
        //state.render(ActivitySearchState.Content(searchResArray))
        //state.render(ActivitySearchState.NothingFound("error"))
    }
}