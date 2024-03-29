package com.playlistmaker.data.network

import com.playlistmaker.data.dto.CompoundSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesMediaSearchApi {
    @GET("/search?entity=song")
    fun searchMusic(@Query("term") text: String): Call<CompoundSearchResponse>

    @GET("/search?entity=song")
    suspend fun searchMusicCoroutines(@Query("term") text: String): CompoundSearchResponse
}