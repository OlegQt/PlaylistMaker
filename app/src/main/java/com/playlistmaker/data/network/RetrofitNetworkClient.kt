package com.playlistmaker.data.network

import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.MusicResponse
import com.playlistmaker.data.dto.MusicSearchRequest
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