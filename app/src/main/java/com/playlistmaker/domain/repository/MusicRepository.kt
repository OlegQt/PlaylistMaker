package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.SearchRequest
import com.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun searchMusicViaCoroutines(searchParams:SearchRequest):Flow<Resource<ArrayList<MusicTrack>>>

    fun searchMusic(searchParams:SearchRequest):Resource<ArrayList<MusicTrack>>
    fun safeMusicSearchHistory(musicList:ArrayList<MusicTrack>)
    fun loadMusicSearchHistory():Resource<ArrayList<MusicTrack>>
    fun deleteAllMusicSearchHistory()
}