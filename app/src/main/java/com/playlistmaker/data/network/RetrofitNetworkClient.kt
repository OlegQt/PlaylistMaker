package com.playlistmaker.data.network

import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.SearchResponse
import com.playlistmaker.domain.models.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient<SearchRequest,SearchResponse> {

    private val baseUrl = "https://itunes.apple.com"


    // retrofit initialisation will come with class member initialisation
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaApi: ItunesMediaSearchApi = retrofit.create(ItunesMediaSearchApi::class.java)
    override fun doRequest(request: SearchRequest): SearchResponse {
        when (request) {
            is SearchRequest.MusicSearchRequest -> {
                val response = mediaApi.searchMusic(request.searchParam).execute()
                val body = response.body() ?: SearchResponse()
                body.resultCode = response.code()
                return body
            }
        }
    }


}