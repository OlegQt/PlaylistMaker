package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListController {
    suspend fun savePlaylist(playList: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>
}