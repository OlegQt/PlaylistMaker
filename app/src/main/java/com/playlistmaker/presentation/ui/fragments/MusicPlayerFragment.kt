package com.playlistmaker.presentation.ui.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.playlistmaker.R
import com.playlistmaker.appstart.App
import com.playlistmaker.databinding.FragmentPlayerBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import com.playlistmaker.presentation.ui.activities.ActivityPlayerB
import com.playlistmaker.presentation.ui.customview.PlaybackButtonView
import com.playlistmaker.presentation.ui.musicservice.MusicPlayerService
import com.playlistmaker.presentation.ui.musicservice.MusicPlayerState
import com.playlistmaker.presentation.ui.recycleradapter.PlayListAdapter
import com.playlistmaker.presentation.ui.viewmodel.FragmentMusicPlayerVm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_TRACK = "MUSIC_TRACK"

class MusicPlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val vm: FragmentMusicPlayerVm by viewModel()

    private var musicTrack = MusicTrack()

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private val playListFromDB = mutableListOf<PlayList>()
    private val adapterPlayList =
        PlayListAdapter(playListFromDB, PlayListAdapter.RecyclerType.SMALL) {
            onPlayListClick(playListFromDB[it])
        }

    private var musicServiceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // System back pressed listener
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitFragmentAndStopService()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)

        // Снимаем аргументы переданные через activity
        arguments?.let { content ->
            var param: MusicTrack? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                param = content.getParcelable(ARG_TRACK, MusicTrack::class.java)
            } else param = content.getParcelable(ARG_TRACK)

            param?.let {
                musicTrack = it
                // Подгружаем musicTrack into viewModel
                vm.loadCurrentMusicTrack(trackToPlay = musicTrack)
            }
        }

        // TODO: Delete this comment (new fun)
        startMusicPlayerService(musicTrackToPlay = musicTrack)

        vm.playingTime.observe(viewLifecycleOwner) {
            binding.playerPlayTime.text = it.toTimeMmSs()
        }

        vm.currentMusTrack.observe(viewLifecycleOwner) { this.showTrackInfo(it) }

        vm.errorMsg.observe(viewLifecycleOwner) { showSnackBar(it) }

        vm.bottomSheetIsShown.observe(viewLifecycleOwner) {
            if (it) bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            else bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        // Ниже подписываемся на обновление плейлистов из базы данных
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.playlistState.collect {
                    updatePlayListFromDB(it)
                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerBtnBack.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                exitFragmentAndStopService()
            }
        }

        binding.playerBtnPlay.setOnClickListener { vm.pushPlayPauseButton() }

        binding.btnPlayback.setOnClickListener { vm.pushPlayPauseButton() }

        binding.addToFavBtn.setOnClickListener { vm.pushAddToFavButton() }

        binding.addToFavBtn.setOnLongClickListener { vm.showAllFavouriteTracks() }

        binding.btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.btnNewPlaylist.setOnClickListener {
            Log.e("LOG", "PUSH btn CreateNEwPlaylist")
            parentFragmentManager.commit {
                replace(R.id.fragment_holder, NewPlaylistFragment())
                addToBackStack(null)
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset
                }
            })
        }

        // Добавляем адаптер для просмотра списка плейлистов внутрь Recycler
        binding.playlistsRecycler.adapter = this.adapterPlayList
        binding.playlistsRecycler.layoutManager = LinearLayoutManager(this.requireContext())

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

    private fun updatePlayListFromDB(state: FragmentPlaylistsState) {
        when (state) {
            is FragmentPlaylistsState.Content -> {
                this.playListFromDB.clear()
                this.playListFromDB.addAll(state.playLists)
                adapterPlayList.notifyItemRangeChanged(0, playListFromDB.size)
            }

            is FragmentPlaylistsState.NothingFound -> {}
        }
    }

    override fun onPause() {
        vm.showServiceNotification()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        vm.hideServiceNotification()
    }

    override fun onDestroyView() {
        musicServiceConnection?.let { connection ->
            requireContext().unbindService(connection)

            // Надо ли дополнительно уничтожать сервис такой функцией?
            //requireContext().stopService(Intent(requireContext(),MusicPlayerService::class.java))
        }

        _binding = null
        super.onDestroyView()
    }

    private fun exitFragmentAndStopService() {
        musicServiceConnection?.let {
            requireContext().unbindService(it)
        }
        musicServiceConnection = null

        (requireActivity() as ActivityPlayerB).exitPlayerActivity()
    }

    private fun Long.toTimeMmSs(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
    }

    private fun onPlayListClick(playListClicked: PlayList) {
        vm.onPlayListClick(clickedPlayList = playListClicked)
    }

    private fun startMusicPlayerService(musicTrackToPlay: MusicTrack = MusicTrack()) {
        musicServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                val mService = (service as MusicPlayerService.LocalBinder).getService()

                vm.setNewMusicPlayerService(newPlayer = mService)

                lifecycleScope.launch {
                    mService.playerState.collect { updateUiByPlayerState(it) }
                }

            }

            override fun onServiceDisconnected(arg0: ComponentName) {}
        }

        Intent(requireContext(), MusicPlayerService::class.java).also {
            it.putExtra(App.MUSIC_PLAYER_SERVICE_TRACK_MODEL, musicTrackToPlay)

            musicServiceConnection?.let { connection ->
                requireContext().bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private fun updateUiByPlayerState(playerState: MusicPlayerState) {
        when (playerState) {
            is MusicPlayerState.MusicPlaying -> {
                binding.playerPlayTime.text = playerState.playProgress?.toLong()?.toTimeMmSs()
                binding.btnPlayback.changeState(PlaybackButtonView.PlaybackButtonState.PAUSE)
            }

            is MusicPlayerState.MusicPaused -> {
                binding.playerPlayTime.text = playerState.playProgress?.toLong()?.toTimeMmSs()
                binding.btnPlayback.changeState(PlaybackButtonView.PlaybackButtonState.PLAY)
            }

            is MusicPlayerState.MusicPlayingCompleted -> {

            }

            is MusicPlayerState.PlayerLoad -> {
                // Unable to push play button until track is not fully loaded
                binding.btnPlayback.isEnabled = false
            }

            is MusicPlayerState.MusicReadyToPlay -> {
                binding.btnPlayback.isEnabled = true
            }
        }
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

