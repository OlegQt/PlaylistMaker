package com.playlistmaker.domain.db

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun savePlaylist(playListToSave: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>

    suspend fun loadPlayListById(id:Long):PlayList

    suspend fun clearDB()

    suspend fun updatePlaylist(playList: PlayList)

    suspend fun  saveMusicTrackInTrackListBD(musicTrack: MusicTrack)

    fun loadAllTracksFromTrackListDB():Flow<List<MusicTrack>>

    suspend fun loadTracksMatchedId(ids: List<Long>):List<MusicTrack>
}