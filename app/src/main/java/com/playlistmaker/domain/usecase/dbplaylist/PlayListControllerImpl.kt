package com.playlistmaker.domain.usecase.dbplaylist

import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListControllerImpl(private val playListRepository: PlayListRepository) :
    PlayListController {
    override suspend fun savePlaylist(playList: PlayList) {
        playListRepository.savePlaylist(playListToSave = playList)
    }

    override fun loadAllPlayLists(): Flow<List<PlayList>> =
        playListRepository.loadAllPlayLists()

    override suspend fun loadPlayListById(id: Long): PlayList {
        return playListRepository.loadPlayListById(id)
    }

    override suspend fun clearBD() {
        playListRepository.clearDB()
    }

    override suspend fun updatePlayList(playListToUpdate: PlayList) {
        playListRepository.updatePlaylist(playList = playListToUpdate)
    }

    override suspend fun deleteTrackFromPlayList(playListToUpdate: PlayList, trackId: Long) {
        playListRepository.deleteTrackFromPlayList(
            playListToUpdate,
            idTrackToDelete = trackId)
    }

    override suspend fun safeMusicTrackToTrackListDB(musicTrackToSafe: MusicTrack) {
        playListRepository.saveMusicTrackInTrackListBD(musicTrackToSafe)
    }

    override fun loadAllTracksFromTrackListDB(): Flow<List<MusicTrack>> {
        return playListRepository.loadAllTracksFromTrackListDB()
    }

    override suspend fun getMusicTracksMatchedIds(ids: List<Long>): List<MusicTrack> {
        return playListRepository.loadTracksMatchedId(ids)
    }

    override fun getFlowMusicTracksMatchedIds(ids: List<Long>): Flow<List<MusicTrack>> {
        return playListRepository.flowLoadTracksMatchedId(ids)
    }
}