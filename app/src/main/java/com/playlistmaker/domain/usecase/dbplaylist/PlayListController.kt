package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListController {
    suspend fun savePlaylist(playList: PlayList)

    suspend fun loadAllPlayLists(): Flow<List<PlayList>>

    suspend fun  clearBD()

    suspend fun updatePlayList(playListToUpdate: PlayList)
}