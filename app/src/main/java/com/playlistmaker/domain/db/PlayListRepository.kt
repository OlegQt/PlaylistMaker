package com.playlistmaker.domain.db

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun savePlaylist(playListToSave: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>

    fun loadPlayListById(id:Long):Flow<PlayList>

    suspend fun clearDB()

    suspend fun updatePlaylist(playList: PlayList)

    suspend fun deleteTrackFromPlayList(playListToUpdate: PlayList, idTrackToDelete:Long)

    suspend fun  saveMusicTrackInTrackListBD(musicTrack: MusicTrack)

    fun loadAllTracksFromTrackListDB():Flow<List<MusicTrack>>

    suspend fun loadTracksMatchedId(ids: List<Long>):List<MusicTrack>

    suspend fun checkIfTrackIsUnused(id:Long)

    fun flowLoadTracksMatchedId(ids: List<Long>):Flow<List<MusicTrack>>
}