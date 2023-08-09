package com.playlistmaker.presentation.ui.fragments.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.FragmentFavouriteTracksState
import com.playlistmaker.presentation.ui.viewmodel.FragmentFavouriteTracksVm
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouriteTracksFragment : Fragment() {

    private lateinit var binding:FragmentFavouriteTracksBinding
    private val vm: FragmentFavouriteTracksVm by viewModel()
    private var favouriteTracksList = arrayListOf<MusicTrack>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater,container,false)

        vm.getFragmentState().observe(viewLifecycleOwner){
            when(it){
                is FragmentFavouriteTracksState.NothingFound -> {
                    binding.stubLayout.visibility = View.VISIBLE
                    binding.favouriteTracksRecycler.visibility = View.GONE
                }
                is FragmentFavouriteTracksState.Content -> {
                    binding.stubLayout.visibility = View.GONE
                    binding.favouriteTracksRecycler.visibility = View.VISIBLE
                    // TODO: скопировать треки во внутренний список и обновить адаптер recycler
                    // TODO: Don't forget to check if arrayList is empty
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO: Извлечение параметров для view элементов ниже
        //binding.txtStubMainError.text=arguments?.getString(PARAM_TITLE)

    }

    companion object{
        // На данном этапе не требуется передавать никаких параметров, однако в задании
        // жестко задано создавать фрагмент через instance
        private const val PARAM_TITLE = "PARAM_TITLE"
        fun newInstance(strParam:String)= FavouriteTracksFragment().apply {
            arguments = Bundle().apply { putString(PARAM_TITLE,strParam) }
        }
    }

}