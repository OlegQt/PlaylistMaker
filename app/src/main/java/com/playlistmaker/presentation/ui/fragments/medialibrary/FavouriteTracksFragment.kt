package com.playlistmaker.presentation.ui.fragments.medialibrary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.logic.SearchTrackAdapter
import com.playlistmaker.presentation.models.FragmentFavouriteTracksState
import com.playlistmaker.presentation.ui.activities.ActivityPlayer
import com.playlistmaker.presentation.ui.viewmodel.FragmentFavouriteTracksVm
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouriteTracksFragment : Fragment() {
    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!

    private val vm: FragmentFavouriteTracksVm by viewModel()
    private var favouriteTracksList = arrayListOf<MusicTrack>()
    private val adapter = SearchTrackAdapter(favouriteTracksList) {
        startPlayerActivity(favouriteTracksList[it].apply { isFavourite=true })
    }

    private fun startPlayerActivity(musicTrackToPlay: MusicTrack) {
        val intentPlayerActivity = Intent(requireContext(), ActivityPlayer::class.java)
        intentPlayerActivity.putExtra(MusicTrack.TRACK_KEY, musicTrackToPlay)
        startActivity(intentPlayerActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)



        vm.getFragmentState().observe(viewLifecycleOwner) {
            when (it) {
                is FragmentFavouriteTracksState.NothingFound -> {
                    binding.stubLayout.visibility = View.VISIBLE
                    binding.favouriteTracksRecycler.visibility = View.GONE
                }

                is FragmentFavouriteTracksState.Content -> {
                    binding.stubLayout.visibility = View.GONE
                    binding.favouriteTracksRecycler.visibility = View.VISIBLE
                    // TODO: скопировать треки во внутренний список и обновить адаптер recycler
                    favouriteTracksList.clear()
                    favouriteTracksList.addAll(it.tracksList)
                    adapter.notifyDataSetChanged()


                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.favouriteTracksRecycler.adapter = adapter
        binding.favouriteTracksRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        vm.loadFavouriteTracks()
    }

    companion object {
        // На данном этапе не требуется передавать никаких параметров, однако в задании
        // жестко задано создавать фрагмент через instance
        private const val PARAM_TITLE = "PARAM_TITLE"
        fun newInstance(strParam: String) = FavouriteTracksFragment().apply {
            arguments = Bundle().apply { putString(PARAM_TITLE, strParam) }
        }
    }

}