package com.playlistmaker.data

import com.playlistmaker.data.dto.MusicResponse

interface NetworkClient {
    fun doRequest(dto: Any): MusicResponse
}