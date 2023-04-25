package com.playlistmaker.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesMediaSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ItunesMediaSearchResult>
}