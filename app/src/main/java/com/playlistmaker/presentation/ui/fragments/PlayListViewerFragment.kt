package com.playlistmaker.presentation.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
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

        lifecycleScope.launch {
            vm.screenState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                setScreenState(it)
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    fun Long.getStringMm() = SimpleDateFormat("m", Locale.getDefault()).format(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Достаем информацию о листе через базу данных, используя
        // переданный в аргументах id листа
        vm.evaluatePlayList(param)

        // Разбираемся со шторкой
        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Инициализируем адаптер
        binding.trackRecycler.layoutManager = LinearLayoutManager(this.requireContext())
        binding.trackRecycler.adapter = musTrackAdapter
        // Инициализируем слушатель долгого нажатия на VH адаптера
        musTrackAdapter.longClickListener = SearchTrackAdapter.OnTrackLongClick {
            //vm.setError("VERY long click on ${tracksInPlayList[it].trackName}")
            //vm.deleteTrackFromPlaylist(trackId = tracksInPlayList[it].trackId)
            this.askToDeleteTrack(clickedTrackId = tracksInPlayList[it].trackId)
        }


        binding.topAppBar.setNavigationOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigateUp()
        }

        binding.btnPlaylistShare.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.root_placeholder, PlayListEditorFragment.setArg(param))
                addToBackStack(null)
            }
        }

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
        // Анонимный класс ниже является слушателем, реагирующем на успешность
        // загрузки изображения в glide
        val stubReplacer = object : RequestListener<Drawable?> {
            // Called when an exception occurs during a load
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {

                Log.e("LOG", "onLoadFailed")
                with(binding.playListCover) {
                    // Установите параметры ширины и высоты на wrap_content
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                    // Вычисляем плотность пикселей на экране
                    val dpi = resources.displayMetrics.density
                    val marginLayoutParams = layoutParams as MarginLayoutParams
                    marginLayoutParams.topMargin = (VERTICAL_MARGIN_COVER_PLACEHOLDER * dpi).toInt()
                    marginLayoutParams.bottomMargin = (VERTICAL_MARGIN_COVER_PLACEHOLDER * dpi).toInt()
                    // Обновляем ImageView, чтобы применить изменения
                    requestLayout()
                }
                return false
            }

            // Called when a load completes successfully
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        }

        Glide
            .with(binding.root)
            .load(playListInfo.cover)
            .placeholder(R.drawable.no_track_found)
            //.override(1000, 1000)
            .listener(stubReplacer)
            .into(binding.playListCover)

        with(binding) {
            txtPlaylistName.text = playListInfo.name
            txtPlaylistDescription.text = playListInfo.description
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

            txtPlaylistLength.text = "$playListLength * $trackQuantity"
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

    companion object {
        const val PLAYLIST_ID_ARG = "ARG"
        const val PARAM_BEFORE_ON_CREATE = -1L
        const val VERTICAL_MARGIN_COVER_PLACEHOLDER = 56
    }

    sealed class ScreenState {
        class Content(val playList: PlayList, val musicTracks: List<MusicTrack>) : ScreenState()
        class PlayListData(val playList: PlayList) : ScreenState()
        class NoData(val message: String?) : ScreenState()
    }
}