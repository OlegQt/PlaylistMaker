package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource

interface MusicRepository {
    fun searchMusic(searchParams:Any):Resource<ArrayList<MusicTrack>>
    fun safeMusicSearchHistory(musicList:ArrayList<MusicTrack>)
    fun loadMusicSearchHistory():Resource<ArrayList<MusicTrack>>
    fun deleteAllMusicSearchHistory()
}