package com.playlistmaker.domain.db

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun savePlaylist(playListToSave: PlayList)

    suspend fun loadAllPlayLists(): Flow<List<PlayList>>

    suspend fun clearDB()

    suspend fun updatePlaylist(playList: PlayList)

    suspend fun  saveMusicTrackInTrackListBD(musicTrack: MusicTrack)

    suspend fun loadAllTracksFromTrackListDB():Flow<List<MusicTrack>>
}