package com.playlistmaker.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListEditorVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListEditorFragment : NewPlaylistFragment() {
    override val vm:FragmentPlayListEditorVm by viewModel()
    //override var _binding: FragmentNewPlaylistBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val playListId = it.getLong(ARG_PARAM_1)
            vm.loadPlaylistById(id = playListId)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        with(binding){
            topAppBar.title = "Редактировать"

            btnCreatePlaylist.text ="Сохранить"

            txtPlaylistDescription.doAfterTextChanged {
                vm.changeDescription(newDescription = it.toString())
            }

            txtPlaylistName.doAfterTextChanged {
                vm.changePlayListName(it.toString())
            }
        }

        with(vm){
            errorMsg.observe(viewLifecycleOwner){
                (requireActivity() as AlertMessaging).showAlertDialog(it)
            }

            btnCreateEnable.observe(viewLifecycleOwner) {
                binding.btnCreatePlaylist.isEnabled = it

                if (it) binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.yp_blue))
                else binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.Text_Grey))

            }
        }

        return binding.root
    }

    companion object{
        private const val ARG_PARAM_1 = "param_id_playlist"

        fun setArg(playListId:Long)=PlayListEditorFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_PARAM_1,playListId)
            }
        }
    }
}