package com.playlistmaker.domain.db

import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun savePlaylist(playListToSave: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>
}