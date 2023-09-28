package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListControllerImpl(private val playListRepository:PlayListRepository):PlayListController {
    override suspend fun savePlaylist(playList: PlayList) {
        playListRepository.savePlaylist(playListToSave = playList)
    }

    override suspend fun loadAllPlayLists(): Flow<List<PlayList>> = playListRepository.loadAllPlayLists()
    override suspend fun clearBD() {
        playListRepository.clearDB()
    }
}