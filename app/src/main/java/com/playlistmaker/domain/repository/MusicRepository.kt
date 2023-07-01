package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.MusicTrack

interface MusicRepository {
    fun searchMusic(searchParams:Any):ArrayList<MusicTrack>
}