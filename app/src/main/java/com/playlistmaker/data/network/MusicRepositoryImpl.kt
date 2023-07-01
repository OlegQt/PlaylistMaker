package com.playlistmaker.data.network

import com.playlistmaker.data.NetworkClient
import com.playlistmaker.data.dto.MusicSearchRequest
import com.playlistmaker.data.dto.MusicSearchResponse
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.domain.api.MusicRepository

class MusicRepositoryImpl(private val networkClient: NetworkClient):MusicRepository {
    override fun searchMusic(songName: String): ArrayList<MusicTrackDto> {
        val response = networkClient.doRequest(MusicSearchRequest(songName))
        when(response.resultCode){
            200 ->{
                return (response as MusicSearchResponse).results
            }
            else -> return ArrayList()
        }
    }
}