package com.playlistmaker.data.repository

import com.playlistmaker.data.db.playlist.PlayListDB
import com.playlistmaker.data.db.playlist.PlayListMapper
import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val db: PlayListDB,
    private val mapper: PlayListMapper
) : PlayListRepository {
    override suspend fun savePlaylist(playListToSave: PlayList) {
        db.playListDao().addNewPlayList(playList = mapper.convertToDao(playListToSave))
    }

    override suspend fun loadAllPlayLists(): Flow<List<PlayList>> {
        return db.playListDao().getAllPlayLists()
            .map { it.map { entity -> mapper.convertFromDao(entity) } }

    }

    override suspend fun clearDB() {
        db.playListDao().deleteAllPlaylists()
    }

    override suspend fun updatePlaylist(playList: PlayList) {
        db.playListDao().updatePlaylist(mapper.convertToDao(playList))
    }
}