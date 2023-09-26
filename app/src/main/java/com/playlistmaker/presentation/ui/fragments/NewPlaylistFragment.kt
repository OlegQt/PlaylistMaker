package com.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.databinding.FragmentPlaylistsBinding
import com.playlistmaker.databinding.FragmentSettingsBinding
import com.playlistmaker.presentation.ui.viewmodel.FragmentSettingsVm
import com.playlistmaker.presentation.ui.viewmodel.PlayListVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private val vm: PlayListVm by viewModel()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        vm.btnCreateEnable.observe(viewLifecycleOwner){
            binding.btnCreatePlaylist.isEnabled = it
        }

        setBehaviour()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    fun setBehaviour(){
        binding.txtPlaylistName.doAfterTextChanged {
            vm.changePlayListName(it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}