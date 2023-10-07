package com.playlistmaker.presentation.ui.fragments.medialibrary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlaylistsBinding
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import com.playlistmaker.presentation.ui.fragments.PlayListEditorFragment
import com.playlistmaker.presentation.ui.fragments.recycleradapter.PlayListAdapter
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListsVm
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {
    private val vm: FragmentPlayListsVm by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playListFromDB = mutableListOf<PlayList>()
    private val playlistAdapter =
        PlayListAdapter(playListFromDB, PlayListAdapter.RecyclerType.LARGE) {
            navigateToPlayList(playListToSend = playListFromDB[it])
        }

    private fun navigateToPlayList(playListToSend: PlayList) {
        // Инициализируем навигатор
        val navigator = parentFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
        val navController = navigator.navController
        navController.navigate(R.id.action_mediaLibraryFragment_to_playListEditorFragment,

            Bundle().apply {
                // По заданию, передаем только id
                putLong(PlayListEditorFragment.PLAYLIST_ID_ARG, playListToSend.id)
            })
    }

    private fun setScreenState(newState: FragmentPlaylistsState) {
        when (newState) {
            is FragmentPlaylistsState.NothingFound -> {
                binding.favouriteTracksRecycler.visibility = View.GONE
                binding.stubLayout.visibility = View.VISIBLE
            }

            is FragmentPlaylistsState.Content -> {
                binding.favouriteTracksRecycler.visibility = View.VISIBLE
                binding.stubLayout.visibility = View.GONE
                // TODO: скопировать плейлисты во внутренний список и обновить адаптер recycler
                // TODO: Don't forget to check if map is empty
                playListFromDB.clear()
                playListFromDB.addAll(newState.playLists)
                playlistAdapter.notifyItemRangeChanged(0, playListFromDB.size)
            }

            else -> {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
            (requireActivity() as AlertMessaging).showSnackBar(throwable.message.toString())
            // Вызов показа SnackBar or Toast напрямую приводит к крашу, в случае смены темы приложения
        }) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.playlistState.collect { setScreenState(it) }
            }
        }

        vm.errorMsg.observe(viewLifecycleOwner) {
            (requireActivity() as AlertMessaging).showSnackBar(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewPlaylist.setOnClickListener {
            val navigator =
                parentFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
            val navController = navigator.navController
            navController.navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        binding.btnClearDb.setOnClickListener { vm.clearPlayListBD() }

        binding.favouriteTracksRecycler.adapter = playlistAdapter
        binding.favouriteTracksRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onResume() {
        super.onResume()
        Log.e("LOG_TAG", "Resume playlists")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // На данном этапе не требуется передавать никаких параметров, однако в задании
        // жестко задано создавать фрагмент через instance
        fun newInstance() = PlayListsFragment()
    }
}