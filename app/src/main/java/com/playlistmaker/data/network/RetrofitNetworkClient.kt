package com.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.SearchResponse
import com.playlistmaker.domain.models.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.getKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val mediaApi: ItunesMediaSearchApi,
    private val context: Context
) :
    NetworkClient<SearchRequest, SearchResponse> {

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

    override suspend fun doSuspendRequest(request: SearchRequest): SearchResponse {
        if (request !is SearchRequest.MusicSearchRequest) return SearchResponse()
        if (!isConnected()) return SearchResponse()

        return withContext(Dispatchers.IO) {
            try {
                val response = mediaApi.searchMusicCoroutines(request.searchParam)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                SearchResponse().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}