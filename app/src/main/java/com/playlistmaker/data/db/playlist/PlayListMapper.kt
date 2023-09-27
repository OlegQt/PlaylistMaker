package com.playlistmaker.data.db.playlist

import com.playlistmaker.domain.models.PlayList

class PlayListMapper {
    fun convertFromDao(playListEntity: PlayListEntity):PlayList{
        return PlayList(
            playListEntity.id,
            playListEntity.name,
            playListEntity.description,
            playListEntity.cover,
            playListEntity.trackList,
            playListEntity.quantity
        )
    }

    fun convertToDao(playList: PlayList):PlayListEntity{
        return PlayListEntity(
            playList.id,
            playList.name,
            playList.description,
            playList.cover,
            playList.trackList,
            playList.quantity
        )
    }
}