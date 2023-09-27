package com.playlistmaker.presentation.ui.fragments.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlaylistsBinding
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.logic.PlayListAdapter
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListsVm
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {
    private val vm: FragmentPlayListsVm by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playListFromDB = mutableListOf<PlayList>()
    private val playlistAdapter = PlayListAdapter(playListFromDB)

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
                playlistAdapter.notifyDataSetChanged()


            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.playlistState.collect { setScreenState(it) }
            }
        }

        vm.errorMsg.observe(viewLifecycleOwner){
            Snackbar.make(binding.stubLayout,it,Snackbar.LENGTH_INDEFINITE).setAction("OK") {}.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.loadPlaylists()

        //Временная заглушка для кнопки обновления плейлистов
        binding.btnNewPlaylist.setOnClickListener {
            val navigator =
                requireActivity().supportFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
            val navController = navigator.navController
            navController.navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)

        }

        binding.favouriteTracksRecycler.adapter = playlistAdapter
        binding.favouriteTracksRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()

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