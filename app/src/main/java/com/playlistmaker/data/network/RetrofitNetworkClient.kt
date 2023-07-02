package com.playlistmaker.data.network

import com.playlistmaker.presentation.models.Msgcode
import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.MusicResponse
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.dto.MusicSearchResponse
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.models.MusicTrack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient :NetworkClient{

    private val baseUrl = "https://itunes.apple.com"


    // retrofit initialisation will come with class member initialisation
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaApi: ItunesMediaSearchApi = retrofit.create(ItunesMediaSearchApi::class.java)

    override fun doRequest(dto: Any): MusicResponse {
        return if(dto is MusicSearchRequest){
            val response = mediaApi.searchMusic(dto.songName).execute()
            val body = response.body() ?: MusicResponse()
            body.resultCode = response.code()
            body
        } else{
            MusicResponse()
        }
    }
}