package com.playlistmaker.data.repository

import com.google.gson.Gson
import com.playlistmaker.data.db.favourite.MusicDB
import com.playlistmaker.data.db.playlist.PlayListMapper
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.db.PlayListRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val db: MusicDB,
    private val mapper: PlayListMapper,
    private val trackMapper: MusicTrackMapper,
    private val gSon: Gson
) : PlayListRepository {
    override suspend fun savePlaylist(playListToSave: PlayList) {
        db.playListDao().addNewPlayList(playList = mapper.convertToDao(playListToSave))
    }

    // Загружает все плейлисты
    override fun loadAllPlayLists(): Flow<List<PlayList>> {
        return db.playListDao().getAllPlayLists()
            .map { it.map { entity -> mapper.convertFromDao(entity) } }

    }

    override fun loadPlayListById(id: Long): Flow<PlayList> {
        return db.playListDao().getPlayListById(id).map {
            mapper.convertFromDao(it)
        }

        //mapper.convertFromDao(db.playListDao().getPlayListById(playlistId = id))
    }

    // Полное стирание всех плейлистов
    override suspend fun clearDB() {
        db.playListDao().deleteAllPlaylists()
        db.trackListDao().deleteAll()
    }

    // Обновление плейлиста
    override suspend fun updatePlaylist(playList: PlayList) {
        db.playListDao().updatePlaylist(mapper.convertToDao(playList))
    }

    override suspend fun deleteTrackFromPlayList(
        playListToUpdate: PlayList,
        idTrackToDelete: Long
    ) {
        // Считываем id всех треков из строки в массив
        val listOfTracks: Array<Long> =
            extractTracksFromGson(playListToUpdate.trackList)

        // Обновляем плейлист (перезаписываем список треков и их количество)
        if (listOfTracks.contains(idTrackToDelete)) {
            val betta = listOfTracks.filter { it != idTrackToDelete }
            val newListOfTrack = Gson().toJson(betta)
            updatePlaylist(playListToUpdate.copy(trackList = newListOfTrack, quantity = betta.size))
        }
    }

    // Сохранение музыкального трека, добавленного в какой-либо плейлист внутри базы треков
    override suspend fun saveMusicTrackInTrackListBD(musicTrack: MusicTrack) {
        db.trackListDao().addTrackToList(trackMapper.mapToDao(musicTrack))
    }

    override suspend fun loadTracksMatchedId(ids: List<Long>): List<MusicTrack> {
        return loadAllTracksFromTrackListDB().first()
            .filter { musicTrack -> ids.contains(musicTrack.trackId) }
    }

    override fun loadAllTracksFromTrackListDB(): Flow<List<MusicTrack>> {
        return db.trackListDao().readAllTrackList().map {
            it.map { entity -> trackMapper.mapFromDao(entity) }
        }
    }

    override suspend fun checkIfTrackIsUnused(id: Long) {
        var isUsed = false

        // Извлечение всех треков из базы
        val temp = db.playListDao().getAllPlayLists()
            .map { it.map { entity -> mapper.convertFromDao(entity) } }.first()

        // Просматриваем все плейлисты на наличие трека
        // Если находим искомый трек, помечаем флажок isUsed
        temp.forEach {
            if (extractTracksFromGson(it.trackList).contains(id)) {
                isUsed = true
            }
        }
        if (!isUsed) db.trackListDao().deleteTrackById(id)

    }

    override fun flowLoadTracksMatchedId(ids: List<Long>): Flow<List<MusicTrack>> {
        return loadAllTracksFromTrackListDB().map {
            it.filter { musTrack ->
                ids.contains(musTrack.trackId)
            }
        }
    }

    private fun extractTracksFromGson(jSonTrackList: String): Array<Long> {
        return if (jSonTrackList.isEmpty()) arrayOf()
        else return gSon.fromJson(jSonTrackList, Array<Long>::class.java)
    }

    override suspend fun deletePlaylist(playList: PlayList) {
        db.playListDao().deletePlaylist(mapper.convertToDao(playList))
    }
}