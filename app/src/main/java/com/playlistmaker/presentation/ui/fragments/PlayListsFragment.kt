package com.playlistmaker.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlaylistsBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import com.playlistmaker.presentation.ui.fragments.viewmodels.FragmentPlayListsVm
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {
    private val vm: FragmentPlayListsVm by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getFragmentState().observe(viewLifecycleOwner) {
            when (it) {
                is FragmentPlaylistsState.NothingFound -> {
                    binding.favouriteTracksRecycler.visibility = View.GONE
                    binding.stubLayout.visibility = View.VISIBLE
                }

                is FragmentPlaylistsState.Content -> {
                    binding.favouriteTracksRecycler.visibility = View.VISIBLE
                    binding.stubLayout.visibility = View.GONE
                    // TODO: скопировать плейлисты во внутренний список и обновить адаптер recycler
                    // TODO: Don't forget to check if map is empty
                }
            }
        }

        //Временная заглушка для кнопки обновления плейлистов
        binding.btnReload.setOnClickListener {
            (requireActivity() as AlertMessaging).showAlertDialog("Temporal stub message")
        }
    }


    companion object {
        // На данном этапе не требуется передавать никаких параметров, однако в задании
        // жестко задано создавать фрагмент через instance
        fun newInstance() = PlayListsFragment()
    }
}