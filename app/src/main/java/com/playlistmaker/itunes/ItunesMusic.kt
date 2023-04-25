package com.playlistmaker.itunes

import android.util.Log
import com.playlistmaker.Msgcode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesMusic {
    //private val TAG: String = "DEBUG"
    private val baseUrl = "https://itunes.apple.com"
    var trackLst: ArrayList<ItunesTrack>? = null

    // retrofit initialisation will come with class member initialisation
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaApi: ItunesMediaSearchApi = retrofit.create(ItunesMediaSearchApi::class.java)

    // Второй параметр - лямбда из SearchActivity
    // Вызывается только после ответа (или не ответа) от сервера
    fun search(songName: String, doAfterSearch: (Msgcode) -> Unit) {
        val call = mediaApi.search(songName)
        call.enqueue(object : Callback<ItunesMediaSearchResult> {
            override fun onResponse(
                call: Call<ItunesMediaSearchResult>,
                response: Response<ItunesMediaSearchResult>
            ) {
                if (response.code() == 200) {
                    // Если ответ от сервера OK
                    trackLst = response.body()?.results // Считываем тело ответа в лист
                    doAfterSearch.invoke(Msgcode.OK) // Вызываем функцию в SearchActivity  с OK - кодом
                } else doAfterSearch.invoke(Msgcode.Failure) // Вызываем функцию в SearchActivity с FAIL - кодом
            }

            override fun onFailure(call: Call<ItunesMediaSearchResult>, t: Throwable) {
                doAfterSearch.invoke(Msgcode.Failure) // Вызываем функцию в SearchActivity с FAIL - кодом
            }
        })
    }
}