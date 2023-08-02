package com.playlistmaker.data.network

import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.SearchResponse
import com.playlistmaker.domain.models.SearchRequest
import org.koin.java.KoinJavaComponent.getKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val mediaApi:ItunesMediaSearchApi) : NetworkClient<SearchRequest,SearchResponse> {

    // Связываем с интерфейсом ItunesMedia via DI KOIN
    //private val mediaApi:ItunesMediaSearchApi = getKoin().get()

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