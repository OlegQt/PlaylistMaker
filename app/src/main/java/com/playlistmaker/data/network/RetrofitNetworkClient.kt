package com.playlistmaker.data.network

import com.playlistmaker.ui.models.Msgcode
import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.MusicResponse
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.dto.MusicSearchResponse
import com.playlistmaker.data.dto.MusicTrackDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient :NetworkClient{

    private val baseUrl = "https://itunes.apple.com"

    // Удалить потом
    var trackLst: ArrayList<MusicTrackDto>? = null

    // retrofit initialisation will come with class member initialisation
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaApi: ItunesMediaSearchApi = retrofit.create(ItunesMediaSearchApi::class.java)

    // Второй параметр - лямбда из SearchActivity
    // вызывается только после ответа (или не ответа) от сервера
    fun search(songName: String, doAfterSearch: (Msgcode) -> Unit) {
        val call = mediaApi.searchMusic(songName)
        call.enqueue(object : Callback<MusicSearchResponse> {
            override fun onResponse(
                call: Call<MusicSearchResponse>,
                response: Response<MusicSearchResponse>
            ) {
                if (response.code() == 200) {
                    // Если ответ от сервера OK
                    trackLst = response.body()?.results // Считываем тело ответа в лист
                    doAfterSearch.invoke(Msgcode.OK) // Вызываем функцию в SearchActivity  с OK - кодом
                } else doAfterSearch.invoke(Msgcode.Failure) // Вызываем функцию в SearchActivity с FAIL - кодом
            }

            override fun onFailure(call: Call<MusicSearchResponse>, t: Throwable) {
                doAfterSearch.invoke(Msgcode.Failure) // Вызываем функцию в SearchActivity с FAIL - кодом
            }
        })
    }

    override fun doRequest(dto: Any): com.playlistmaker.data.dto.MusicResponse {
        if(dto is MusicSearchRequest){
            val response = mediaApi.searchMusic(dto.songName).execute()
            val body = response.body() ?: MusicResponse()

            body.resultCode = response.code()
            return body
        }
        else{
            return MusicResponse().apply { resultCode = 400 }
        }

    }
}