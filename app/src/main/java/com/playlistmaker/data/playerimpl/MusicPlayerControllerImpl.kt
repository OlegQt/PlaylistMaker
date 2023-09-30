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
    private var currentState = PlayerState.STATE_DEFAULT

    private lateinit var listener: OnPlayerStateListener

    init {
        mediaPlayer.setOnPreparedListener {
            listener.playerStateChanged(PlayerState.STATE_PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            // Register a callback to be invoked when the end of a media source
            // has been reached during playback.
            it.seekTo(0)
            listener.playerStateChanged(PlayerState.STATE_COMPLETE)
        }

        mediaPlayer.setOnErrorListener { mp, what, extra ->
            // Обработка ошибки
            Log.e("LOG", "MediaPlayer Error: what=$what, extra=$extra")
            return@setOnErrorListener true // Возвращайте true, чтобы указать, что вы обработали ошибку
        }
    }

    // THIS FUN SHOULD BE FIRST
    override fun setMusicPlayerStateListener(actualListener: OnPlayerStateListener){
        listener = actualListener
    }

    override fun preparePlayer(musTrackUrl:String) {
        Log.e("LOG","PLAYER CONTROLLER IMPL PREPARE \nURL = $musTrackUrl")
        try {
            mediaPlayer.setDataSource(musTrackUrl)
            mediaPlayer.prepareAsync()
        } catch (t: Throwable) {
            Log.e("LOG","preparePlayer Throwable ${t.printStackTrace()}")
            listener.playerStateChanged(PlayerState.STATE_NEED_RESET)
        }
        catch (e:Exception){
            Log.e("LOG","preparePlayer Exception ${e.message}")
        }
    }

    override fun playMusic() {
        Log.e("LOG","Функция проигрывания внутри контроллера")
        try {
            mediaPlayer.start()
            listener.playerStateChanged(PlayerState.STATE_PLAYING)
        }
        catch (t:Throwable){
            Log.e("LOG","START PLAYER THROWABLE ${t.printStackTrace()}")
        }
        catch (e: IllegalStateException) {
            // Обработка ошибки
            Log.e("LOG", "IllegalStateException: ${e.printStackTrace()}")
        }
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        listener.playerStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun turnOffPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        Log.e("LOG","mediaPlayer.stop()")
        currentState = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPos(): Int = mediaPlayer.currentPosition
    override fun resetPlayer() {
        // Resets the MediaPlayer to its uninitialized state.
        // After calling this method, you will have to initialize it again by setting the data source and calling prepare().
        mediaPlayer.reset()
    }

}