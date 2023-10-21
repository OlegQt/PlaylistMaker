package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListController {
    suspend fun savePlaylist(playList: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>

    fun loadPlayListById(id: Long): Flow<PlayList>

    suspend fun clearBD()

    suspend fun updatePlayList(playListToUpdate: PlayList)

    suspend fun deletePlayList(playList: PlayList)

    suspend fun deleteTrackFromPlayList(playListToUpdate: PlayList, trackId: Long)

    // Ниже функционал для работы с таблицей треков

    suspend fun safeMusicTrackToTrackListDB(musicTrackToSafe: MusicTrack)

    fun loadAllTracksFromTrackListDB(): Flow<List<MusicTrack>>

    suspend fun getMusicTracksMatchedIds(ids: List<Long>): List<MusicTrack>

    fun getFlowMusicTracksMatchedIds(ids: List<Long>): Flow<List<MusicTrack>>

    suspend fun checkIfTrackIsUnused(id: Long)

}