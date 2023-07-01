package com.playlistmaker.presentation.presenters

import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.data.repository.MusicRepositoryImpl
import com.playlistmaker.domain.usecase.SearchMusicUseCase
import com.playlistmaker.presentation.models.SearchActivityView

class SearchActivityPresenter(private val state:SearchActivityView) {
    fun searchMusic(songName:String){
        val musRequest = MusicSearchRequest(songName)
        val musRepo = MusicRepositoryImpl(RetrofitNetworkClient())
        val searchUseCase = SearchMusicUseCase(musRepo)

        val searchResArray = searchUseCase.executeSearch(musRequest)

        state.showAlertDialog(searchResArray.toString())
    }
}