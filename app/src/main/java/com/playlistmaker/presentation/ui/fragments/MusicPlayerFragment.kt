package com.playlistmaker.presentation.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityPlayerBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.activities.ActivityPlayer
import com.playlistmaker.presentation.ui.viewmodel.FragmentMusicPlayerVm
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_TRACK = "MUSIC_TRACK"

class MusicPlayerFragment : Fragment() {


    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: FragmentMusicPlayerVm

    private var musicTrack = MusicTrack()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPlayerBinding.inflate(layoutInflater)

        vm = ViewModelProvider(this)[FragmentMusicPlayerVm::class.java]

        // Снимаем аргументы переданные через activity
        arguments?.let { content ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val param: MusicTrack? = content.getParcelable(ARG_TRACK, MusicTrack::class.java)
                param?.let {
                    musicTrack = it
                    showTrackInfo(musicTrack)
                }
            }
        }

        // Подгружаем musicTrack into viewModel
        vm.loadCurrentMusicTrack(trackToPlay = musicTrack)

        vm.playingTime.observe(viewLifecycleOwner) {
            binding.playerPlayTime.text = it.toTimeMmSs()
        }

        vm.playerState.observe(viewLifecycleOwner) {
            when (it) {
                PlayerState.STATE_PLAYING -> {
                    changeBtnPlayPause(ActivityPlayer.ButtonState.BUTTON_PAUSE)
                    vm.startTrackPlayingTimer()
                }
                PlayerState.STATE_PAUSED -> {
                    changeBtnPlayPause(ActivityPlayer.ButtonState.BUTTON_PLAY)
                }
                PlayerState.STATE_COMPLETE ->{
                    changeBtnPlayPause(ActivityPlayer.ButtonState.BUTTON_PLAY)
                }

                else -> {}
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerBtnPlay.setOnClickListener { vm.pushPlayPauseButton() }

    }

    private fun changeBtnPlayPause(state: ActivityPlayer.ButtonState) {
        if (state == ActivityPlayer.ButtonState.BUTTON_PLAY) {
            binding.playerBtnPlay.setImageResource(R.drawable.play_track)
        } else {
            binding.playerBtnPlay.setImageResource(R.drawable.playerpause)
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
            PlayerLblAlbum.text = track.collectionName.toString()
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

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

}

