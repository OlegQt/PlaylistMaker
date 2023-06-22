package com.playlistmaker.data

import android.content.Context
import com.google.gson.Gson
import com.playlistmaker.Theme.App
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.api.MusicTrackRepository
import com.playlistmaker.domain.models.MusicTrack

class MusicTrackRepositoryImpl(context: Context): MusicTrackRepository{
    private val sharedPreferences = context.getSharedPreferences(App.PREFERENCES, Context.MODE_PRIVATE)

    override fun getCurrentMusicTrack(): MusicTrack? {
        // Загрузили трек
        val jsonTrack = sharedPreferences.getString(App.CURRENT_PLAYING_TRACK, "")

        // Если трек загружен успешно форматируем в класс DTO и делаем mapping
        return if(jsonTrack.isNullOrEmpty()) null
        else {
            val jTrack = Gson().fromJson(jsonTrack, MusicTrackDto::class.java)
            return MusicTrackMapper().mapFromDto(jTrack)
        }
    }

    override fun setCurrentMusicTrack(musicTrack: MusicTrack) {
        // Делаем mapping
        val musicTrackDto = MusicTrackMapper().mapToDto(musicTrack)

        // Сохраняем трек
        val jsonTrack = Gson().toJson(musicTrackDto, MusicTrackDto::class.java)
        sharedPreferences.edit().putString(App.CURRENT_PLAYING_TRACK, jsonTrack).apply()
    }
}