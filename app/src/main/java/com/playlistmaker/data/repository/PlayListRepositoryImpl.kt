package com.playlistmaker.data.repository

import com.playlistmaker.data.db.favourite.MusicDB
import com.playlistmaker.data.db.playlist.PlayListMapper
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val db: MusicDB,
    private val mapper: PlayListMapper,
    private val trackMapper: MusicTrackMapper
) : PlayListRepository {
    override suspend fun savePlaylist(playListToSave: PlayList) {
        db.playListDao().addNewPlayList(playList = mapper.convertToDao(playListToSave))
    }

    // Загружает все плейлисты
    override suspend fun loadAllPlayLists(): Flow<List<PlayList>> {
        return db.playListDao().getAllPlayLists()
            .map { it.map { entity -> mapper.convertFromDao(entity) } }

    }

    // Полное стирание всех плейлистов
    override suspend fun clearDB() {
        db.playListDao().deleteAllPlaylists()
    }

    // Обновление плейлиста
    override suspend fun updatePlaylist(playList: PlayList) {
        db.playListDao().updatePlaylist(mapper.convertToDao(playList))
    }

    // Сохранение музыкального трека, добавленного в какой-либо плейлист внутри базы треков
    override suspend fun saveMusicTrackInTrackListBD(musicTrack: MusicTrack) {
        db.trackListDao().addTrackToList(trackMapper.mapToDao(musicTrack))
    }

    override suspend fun loadAllTracksFromTrackListDB(): Flow<List<MusicTrack>> {
        return db.trackListDao().readAllTrackList().map {
            it.map { entity -> trackMapper.mapFromDao(entity) }
        }
    }
}