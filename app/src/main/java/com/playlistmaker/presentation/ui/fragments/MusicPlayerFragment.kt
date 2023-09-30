package com.playlistmaker.presentation.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityPlayerBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.activities.ActivityPlayerB
import com.playlistmaker.presentation.ui.viewmodel.FragmentMusicPlayerVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_TRACK = "MUSIC_TRACK"

class MusicPlayerFragment : Fragment() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val vm: FragmentMusicPlayerVm by viewModel()

    private var musicTrack = MusicTrack()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPlayerBinding.inflate(layoutInflater)

        // Снимаем аргументы переданные через activity
        arguments?.let { content ->
            var param: MusicTrack? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                param = content.getParcelable(ARG_TRACK, MusicTrack::class.java)
            }

            param?.let {
                musicTrack = it
                // Подгружаем musicTrack into viewModel
                vm.loadCurrentMusicTrack(trackToPlay = musicTrack)
            }
        }

        // Подгружаем musicTrack into viewModel
        //vm.loadCurrentMusicTrack(trackToPlay = musicTrack)

        vm.playingTime.observe(viewLifecycleOwner) {
            binding.playerPlayTime.text = it.toTimeMmSs()
        }

        vm.playerState.observe(viewLifecycleOwner) {
            when (it) {
                PlayerState.STATE_PLAYING -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PAUSE)
                    vm.startTrackPlayingTimer()
                }

                PlayerState.STATE_PAUSED -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                    vm.stopTrackPlayingTimer()
                }

                PlayerState.STATE_COMPLETE -> {
                    // Проигрывание трека завершилось
                    changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                }

                else -> {}
            }
        }

        vm.currentMusTrack.observe(viewLifecycleOwner) { this.showTrackInfo(it) }

        vm.errorMsg.observe(viewLifecycleOwner) { showSnackBar(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerBtnPlay.setOnClickListener { vm.pushPlayPauseButton() }

        binding.addToFavBtn.setOnClickListener { vm.pushAddToFavButton() }

        binding.addToFavBtn.setOnLongClickListener { vm.showFavTracks() }

        binding.temporalBtn.setOnClickListener {
            //(requireActivity() as ActivityPlayerB).navigateToNewPlaylist()
            (requireActivity() as ActivityPlayerB).openBottomSheet()
            vm.showPlaylists()
        }
    }

    private fun changeBtnPlayPause(state: ButtonState) {
        when (state) {
            ButtonState.BUTTON_PLAY -> binding.playerBtnPlay.setImageResource(R.drawable.play_track)
            ButtonState.BUTTON_PAUSE -> binding.playerBtnPlay.setImageResource(R.drawable.playerpause)
        }
    }

    private fun showTrackInfo(track: MusicTrack) {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val date = dateFormat.parse(track.releaseDate)
        val milliseconds = date?.time
        val releaseYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(milliseconds)


        // Подгружаем текстовые данные по треку
        with(binding) {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            PlayerLblAlbum.text = track.collectionName
            PlayerLblGenre.text = track.primaryGenreName
            PlayerLblCountry.text = track.country
            PlayerLblFullDuration.text = track.trackTimeMillis.toTimeMmSs()
            PlayerLblYear.text = releaseYear
        }

        // Определяем, есть ли трек в базе данных избранных треков и
        // выбираем соответствующую иконку
        if (track.isFavourite) {
            binding.addToFavBtn.setImageResource(R.drawable.red_heart)
        } else binding.addToFavBtn.setImageResource(R.drawable.like_track)

        // Изменили url картинки
        val artWorkHQ = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        // Получили плотность пикселя
        val px = (requireContext().resources.displayMetrics.densityDpi
                / DisplayMetrics.DENSITY_DEFAULT)

        // Рассчитываем радиус скругления краев imageView
        val radius = 8 * px

        // Загружаем картинку альбома в ImageView
        Glide
            .with(binding.root.context)
            .load(artWorkHQ)
            .placeholder(R.drawable.no_track_found)
            .centerCrop()
            .transform(RoundedCorners(radius)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(binding.playerArtWork)

    }

    private fun showSnackBar(message: String) {
        (requireActivity() as AlertMessaging).showSnackBar(messageToShow = message)
    }

    override fun onPause() {
        super.onPause()
        vm.playPauseMusic(isPlaying = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.turnOffPlayer()
        _binding = null
    }

    private fun Long.toTimeMmSs(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
    }

    companion object {
        fun newInstance(musicTrackToPlay: MusicTrack?): MusicPlayerFragment {
            return MusicPlayerFragment().apply {
                musicTrackToPlay?.let {
                    arguments = Bundle().apply { putParcelable(ARG_TRACK, musicTrackToPlay) }
                }
            }
        }
    }

    enum class ButtonState {
        BUTTON_PLAY,
        BUTTON_PAUSE
    }
}

