package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListController {
    suspend fun savePlaylist(playList: PlayList)

    fun loadAllPlayLists(): Flow<List<PlayList>>

    suspend fun loadPlayListById(id:Long):PlayList

    suspend fun  clearBD()

    suspend fun updatePlayList(playListToUpdate: PlayList)

    suspend fun safeMusicTrackToTrackListDB(musicTrackToSafe:MusicTrack)

    fun loadAllTracksFromTrackListDB():Flow<List<MusicTrack>>

    suspend fun getMusicTracksMatchedIds(ids:List<Long>):List<MusicTrack>

}