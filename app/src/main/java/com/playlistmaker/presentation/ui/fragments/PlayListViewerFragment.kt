package com.playlistmaker.presentation.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.activities.ActivityPlayerB
import com.playlistmaker.presentation.ui.recycleradapter.SearchTrackAdapter
import com.playlistmaker.presentation.ui.recycleradapter.Syntactic
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListViewerVm
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListViewerFragment : Fragment() {
    private val vm: FragmentPlayListViewerVm by viewModel()
    private var param = PARAM_BEFORE_ON_CREATE

    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!

    private val tracksInPlayList = ArrayList<MusicTrack>()
    private val musTrackAdapter = SearchTrackAdapter(tracksInPlayList) {
        startPlayerActivity(musicTrackToPlay = tracksInPlayList[it])
    }

    private lateinit var bottomSheetAdapter: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>

    private val handler = Handler(Looper.getMainLooper())

    fun Long.getStringMm() = SimpleDateFormat("m", Locale.getDefault()).format(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getLong(PLAYLIST_ID_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)


        vm.errorMsg.observe(viewLifecycleOwner) {
            (requireActivity() as AlertMessaging).showAlertDialog(it)
        }

        vm.getStartPlayerCommand.observe(viewLifecycleOwner) {
            startPlayerActivity(it)
        }

        vm.exitTrigger.observe(viewLifecycleOwner) {
            exitFragment()
        }

        lifecycleScope.launch {
            vm.screenState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                setScreenState(it)
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Достаем информацию о листе через базу данных, используя
        // переданный в аргументах id листа
        Log.e("LOG", "before evaluate playlist")
        vm.evaluatePlayList(param)

        // Разбираемся со шторкой

        this.bottomSheetAdapter = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetAdapter.state = BottomSheetBehavior.STATE_HIDDEN

        this.bottomSheetMenu = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN


        // Инициализируем адаптер
        binding.trackRecycler.layoutManager = LinearLayoutManager(this.requireContext())
        binding.trackRecycler.adapter = musTrackAdapter
        // Инициализируем слушатель долгого нажатия на VH адаптера
        musTrackAdapter.longClickListener = SearchTrackAdapter.OnTrackLongClick {
            //vm.setError("VERY long click on ${tracksInPlayList[it].trackName}")
            //vm.deleteTrackFromPlaylist(trackId = tracksInPlayList[it].trackId)
            this.askToDeleteTrack(clickedTrackId = tracksInPlayList[it].trackId)
        }


        binding.topAppBar.setNavigationOnClickListener { exitFragment() }

        binding.btnPlaylistShare.setOnClickListener { sharePlayListIntent() }

        binding.btnPlaylistMenu.setOnClickListener {
            bottomSheetAdapter.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnDeletePlaylist.setOnClickListener {
            showDeletePlaylistDialog("Хотите удалить плейлист ${vm.getCurrentPlyList().name}?")
        }

        binding.btnEditPlaylist.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.root_placeholder, PlayListEditorFragment.setArg(param))
                addToBackStack(null)
            }
        }

        binding.btnSharePlaylist.setOnClickListener { sharePlayListIntent() }


    }

    private fun sharePlayListIntent() {
        if (tracksInPlayList.isEmpty()) {
            (requireActivity() as AlertMessaging).showSnackBar("В этом плейлисте нет списка треков, которым можно поделиться")
        } else {
            val intent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val strExtra = vm.generateMessage(vm.getCurrentPlyList(), tracksInPlayList)
            intent.putExtra(Intent.EXTRA_TEXT, strExtra)
            intent.type = "text/plain"
            //showLightDialog(strExtra)
            requireActivity().startActivity(intent)
        }
    }

    private fun showDeletePlaylistDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogStyle)
            .setMessage(message)
            .setNegativeButton("Нет") { _, _ -> }
            .setPositiveButton("Да") { _, _ ->
                // Вначале удаление всех треков
                vm.deleteMultipleTrack(tracksIds = tracksInPlayList.map { it.trackId })

                // Удаление плейлиста
                vm.deletePlayList()
            }.show()
    }

    private fun exitFragment() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigateUp()
    }

    private fun setScreenState(state: ScreenState) {
        when (state) {
            is ScreenState.Content -> {
                fillRecyclerWithTracks(state.musicTracks)
                bindPlayListInfo(state.playList)
            }

            is ScreenState.NoData -> {}
            is ScreenState.PlayListData -> {}
        }
    }

    private fun bindPlayListInfo(playListInfo: PlayList) {
        binding.playListCover.isVisible = playListInfo.cover.isNotEmpty()
        binding.playListCoverPlaceholder.isVisible = playListInfo.cover.isEmpty()
        handler.postDelayed({ calculatePeekHeight() }, BOTTOM_SHEET_REACTION_TIME_MILLS)

        Glide
            .with(binding.root)
            .load(playListInfo.cover)
            .placeholder(R.drawable.no_track_found)
            .signature(ObjectKey(System.currentTimeMillis()))
            .skipMemoryCache(true)
            .into(binding.playListCover)

        Glide
            .with(binding.root)
            .load(playListInfo.cover)
            .placeholder(R.drawable.no_track_found)
            .signature(ObjectKey(System.currentTimeMillis()))
            .into(binding.imgSmallPlaylistCover)

        with(binding) {
            txtPlaylistName.text = playListInfo.name
            txtPlaylistDescription.text = playListInfo.description
            txtSmallPlaylistName.text = playListInfo.name
        }

    }

    private fun fillRecyclerWithTracks(tracks: List<MusicTrack>) {
        tracksInPlayList.clear()
        tracksInPlayList.addAll(tracks)
        musTrackAdapter.notifyDataSetChanged()


        // Показываем заглушку в случае пустого списка
        with(binding) {
            if (tracksInPlayList.isEmpty()) {
                trackRecycler.visibility = View.GONE
                imgStub.visibility = View.VISIBLE
                txtStubMainError.visibility = View.VISIBLE
            } else {
                trackRecycler.visibility = View.VISIBLE
                imgStub.visibility = View.GONE
                txtStubMainError.visibility = View.GONE
            }

            // Отображение количества треков и общей их продолжительности
            // производим только после загрузки треков
            val playListLength =
                Syntactic.getMinuteEnding(getPlayListDuration(tracks).getStringMm().toInt())
            val trackQuantity = Syntactic.getTrackEnding(tracks.size)

            txtPlaylistLength.text = playListLength
            txtTrackCount.text = trackQuantity
            txtAmountSmall.text = trackQuantity
        }
    }

    private fun getPlayListDuration(tracksInPlaylist: List<MusicTrack>) =
        tracksInPlaylist.sumOf { it.trackTimeMillis }

    private fun startPlayerActivity(musicTrackToPlay: MusicTrack) {
        val intentPlayerActivity = Intent(requireContext(), ActivityPlayerB::class.java)
        intentPlayerActivity.putExtra(MusicTrack.TRACK_KEY, musicTrackToPlay)
        startActivity(intentPlayerActivity)
    }

    private fun askToDeleteTrack(clickedTrackId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить трек? ")
            .setNegativeButton("НЕТ", null)
            .setPositiveButton(
                "ДА"
            ) { p0, p1 ->
                vm.deleteTrackFromPlaylist(trackId = clickedTrackId)
                p0.dismiss()
            }
            .show().apply {
                getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(
                    resources.getColor(
                        R.color.yp_blue,
                        requireActivity().theme
                    )
                )
                getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(
                    resources.getColor(
                        R.color.yp_blue,
                        requireActivity().theme
                    )
                )
            }

    }

    private fun calculatePeekHeight() {
        val screenHeight = binding.root.height
        bottomSheetAdapter.peekHeight = screenHeight - binding.btnPlaylistShare.bottom
        bottomSheetMenu.peekHeight = screenHeight - binding.txtPlaylistName.bottom

        bottomSheetAdapter.state = BottomSheetBehavior.STATE_COLLAPSED

    }


    companion object {
        const val PLAYLIST_ID_ARG = "ARG"
        const val PARAM_BEFORE_ON_CREATE = -1L
        const val VERTICAL_MARGIN_COVER_PLACEHOLDER = 56
        const val BOTTOM_SHEET_REACTION_TIME_MILLS = 150L

    }

    sealed class ScreenState {
        class Content(val playList: PlayList, val musicTracks: List<MusicTrack>) : ScreenState()
        class PlayListData(val playList: PlayList) : ScreenState()
        class NoData(val message: String?) : ScreenState()
    }
}