package com.playlistmaker.data.mapper

import com.playlistmaker.data.db.MusicTrackEntity
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.domain.models.MusicTrack

class MusicTrackMapper {
    fun mapToDto(musicTrack: MusicTrack): MusicTrackDto {
        return MusicTrackDto(
            trackName =musicTrack.trackName ,
            artistName=musicTrack.artistName,
            trackTimeMillis = musicTrack.trackTimeMillis,
            artworkUrl100 = musicTrack.artworkUrl100,
            trackId =musicTrack.trackId,
            collectionName = musicTrack.collectionName,
            releaseDate = musicTrack.releaseDate,
            primaryGenreName = musicTrack.primaryGenreName,
            country = musicTrack.country,
            previewUrl = musicTrack.previewUrl
        )
    }

    fun mapFromDto(musicTrackDto: MusicTrackDto): MusicTrack {
        return MusicTrack(
            trackName =musicTrackDto.trackName ,
            artistName=musicTrackDto.artistName,
            trackTimeMillis = musicTrackDto.trackTimeMillis,
            artworkUrl100 = musicTrackDto.artworkUrl100,
            trackId =musicTrackDto.trackId,
            collectionName = musicTrackDto.collectionName,
            releaseDate = musicTrackDto.releaseDate ?: "0",
            primaryGenreName = musicTrackDto.primaryGenreName,
            country = musicTrackDto.country,
            previewUrl = musicTrackDto.previewUrl,
            isFavourite = false
        )
    }

    fun mapToDao(musicTrack: MusicTrack): MusicTrackEntity = MusicTrackEntity(
        id = musicTrack.trackId,
        trackName = musicTrack.trackName,
        artistName = musicTrack.artistName,
        trackTimeMillis = musicTrack.trackTimeMillis,
        artworkUrl100 = musicTrack.artworkUrl100,
        collectionName = musicTrack.collectionName,
        releaseDate = musicTrack.releaseDate,
        primaryGenreName = musicTrack.primaryGenreName,
        country = musicTrack.country,
        previewUrl = musicTrack.previewUrl,
        timeInset = System.currentTimeMillis()
    )

    fun mapFromDao(musicTrackEntity: MusicTrackEntity): MusicTrack {
        return MusicTrack(
            trackName = musicTrackEntity.trackName,
            artistName = musicTrackEntity.artistName,
            trackTimeMillis = musicTrackEntity.trackTimeMillis,
            artworkUrl100 = musicTrackEntity.artworkUrl100,
            trackId = musicTrackEntity.id,
            collectionName = musicTrackEntity.collectionName,
            releaseDate = musicTrackEntity.releaseDate ?: "0",
            primaryGenreName = musicTrackEntity.primaryGenreName,
            country = musicTrackEntity.country,
            previewUrl = musicTrackEntity.previewUrl,
            isFavourite = false
        )
    }
}