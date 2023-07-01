package com.playlistmaker.data.repository

import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.dto.MusicSearchResponse
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository

class MusicRepositoryImpl(private val networkClient: NetworkClient) : MusicRepository {

    override fun searchMusic(searchParams: Any): ArrayList<MusicTrack> {
        val response = networkClient.doRequest(searchParams)
        return when (response.resultCode) {
            200 -> {
                val resultMusicList:ArrayList<MusicTrack> = ArrayList()

                // Цикл ниже преобразует все данные в данные модели слоя DATA
                (response as MusicSearchResponse).results.forEach {
                    resultMusicList.add(MusicTrackMapper().mapFromDto(it))
                }

                resultMusicList
            }
            else -> ArrayList()
        }
    }
}