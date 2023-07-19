package com.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.playlistmaker.data.dto.CompoundSearchResponse
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.data.network.RetrofitNetworkClient
import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.util.Resource
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

private const val SEARCH_HISTORY = "key_for_search_history"

class MusicRepositoryImpl(private val networkClient: RetrofitNetworkClient, context: Context) :
    MusicRepository {
    private val sharedPreferences: SharedPreferences = KoinJavaComponent.getKoin()
        .get() { parametersOf(context) }
    private val gSon: Gson = KoinJavaComponent.getKoin().get()

    override fun searchMusic(searchParams: SearchRequest): Resource<ArrayList<MusicTrack>> {
        val response = networkClient.doRequest(searchParams)
        return when (response.resultCode) {
            -1 -> Resource.Error(ErrorList.NETWORK_TROUBLES)
            200 -> {
                val resultMusicList: ArrayList<MusicTrack> = ArrayList()
                // Цикл ниже преобразует все данные в данные модели слоя DATA
                (response as CompoundSearchResponse).results.forEach {
                    resultMusicList.add(MusicTrackMapper().mapFromDto(it))
                }

                if (resultMusicList.isEmpty()) return Resource.Error(ErrorList.NOTHING_FOUND)
                else return Resource.Success(resultMusicList)
            }

            else -> Resource.Error(ErrorList.UNKNOWN_ERROR)
        }
    }

    override fun safeMusicSearchHistory(musicList: ArrayList<MusicTrack>) {
        if (musicList.isNotEmpty()) {
            val jSonHistory = gSon.toJson(musicList)
            sharedPreferences.edit().putString(SEARCH_HISTORY, jSonHistory).apply()
        }
    }

    override fun loadMusicSearchHistory(): Resource<ArrayList<MusicTrack>> {
        val jSonHistory = sharedPreferences.getString(SEARCH_HISTORY, "")
        val data = gSon.fromJson(jSonHistory, Array<MusicTrack>::class.java)

        return if (data.isNullOrEmpty()) Resource.Error(ErrorList.NOTHING_FOUND)
        else Resource.Success(data.toCollection(ArrayList<MusicTrack>()))
    }

    override fun deleteAllMusicSearchHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }
}