package com.playlistmaker.data.playerimpl
import android.media.MediaPlayer
import android.util.Log
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import kotlinx.coroutines.flow.Flow

class MusicPlayerControllerImpl() :
    MusicPlayerController {

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    private lateinit var listener: OnPlayerStateListener

    private var isPrepared = false


    // THIS FUN SHOULD BE FIRST
    override fun setMusicPlayerStateListener(actualListener: OnPlayerStateListener){
        listener = actualListener
    }

    override fun preparePlayer(musTrackUrl:String) {
        mediaPlayer.apply {
            setDataSource(musTrackUrl)
            prepareAsync()

            mediaPlayer.setOnPreparedListener {
                isPrepared=true
                listener.playerStateChanged(PlayerState.STATE_PREPARED)
            }

            mediaPlayer.setOnCompletionListener {
                // Register a callback to be invoked when the end of a media source
                // has been reached during playback.
                it.seekTo(0)
                listener.playerStateChanged(PlayerState.STATE_COMPLETE)
            }
        }

    }

    override fun playMusic() {
        try {
            mediaPlayer.start()
            listener.playerStateChanged(PlayerState.STATE_PLAYING)
        }
        catch (t:Throwable){
            Log.e("LOG","START PLAYER THROWABLE ${t.printStackTrace()}")
        }
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        listener.playerStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun turnOffPlayer() {
        mediaPlayer.reset()
        isPrepared = false
    }

    override fun getCurrentPos(): Int = mediaPlayer.currentPosition
    override fun resetPlayer() {
        // Resets the MediaPlayer to its uninitialized state.
        // After calling this method, you will have to initialize it again by setting the data source and calling prepare().
        mediaPlayer.reset()
    }

}