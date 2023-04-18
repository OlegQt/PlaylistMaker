package com.playlistmaker.itunes

import android.util.Log
import com.playlistmaker.Msgcode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesMusic {
    private val TAG: String = "DEBUG"
    private val baseUrl = "https://itunes.apple.com"
    var trackLst:ArrayList<ItunesTrack>? = ArrayList<ItunesTrack>()

    // retrofit initialisation will come with class member initialisation
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mediaApi = retrofit.create(ItunesMediaSearchApi::class.java)

    fun search(songName: String, doAfterSearch: (Msgcode) -> Unit) {
        val call = mediaApi.search(songName)
        call.enqueue(object : Callback<ItunesMediaSearchResult> {
            override fun onResponse(
                call: Call<ItunesMediaSearchResult>,
                response: Response<ItunesMediaSearchResult>
            ) {
                // Есть ответ
                Log.d(TAG, response.code().toString())

                trackLst = response?.body()?.results
                doAfterSearch.invoke(Msgcode.OK)

            }

            override fun onFailure(call: Call<ItunesMediaSearchResult>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                doAfterSearch.invoke(Msgcode.Failure)
            }
        })

    }
}