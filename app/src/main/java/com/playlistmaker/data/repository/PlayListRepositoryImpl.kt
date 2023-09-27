package com.playlistmaker.data.repository

import com.playlistmaker.data.db.playlist.PlayListDB
import com.playlistmaker.data.db.playlist.PlayListMapper
import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val db: PlayListDB,
    private val mapper: PlayListMapper
) : PlayListRepository {
    override suspend fun savePlaylist(playListToSave: PlayList) {
        db.playListDao().addNewPlayList(playList = mapper.convertToDao(playListToSave))
    }

    override fun loadAllPlayLists(): Flow<List<PlayList>> {
        return flow {
            emit(db.playListDao().getAllPlayLists().map { mapper.convertFromDao(it) })
        }
    }
}