package com.playlistmaker.domain.impl

import com.playlistmaker.Logic.Player
import com.playlistmaker.domain.api.MusicPlayerInteractor
import com.playlistmaker.domain.api.MusicTrackRepository

class MusicPlayerInteractorImpl(private val musTrackRepo:MusicTrackRepository,private val musPlayer:Player) :MusicPlayerInteractor{
    override fun preparePlayer() {
        val musTrack = musTrackRepo.getCurrentMusicTrack()
        if (musTrack!=null){
            musPlayer.preparePlayer(musTrack.previewUrl)
        }
    }

    override fun playMusicTrack() {
        musPlayer.playMusic()
    }

    override fun pauseMusicTrack() {
        musPlayer.pausePlayer()
    }

    override fun stopMusicTrack() {
        musPlayer.turnOffPlayer()
    }

    fun getPlayerState() = musPlayer.isPlaying()

    fun getCurrentPlayingPosition()=musPlayer.getDuration()


}