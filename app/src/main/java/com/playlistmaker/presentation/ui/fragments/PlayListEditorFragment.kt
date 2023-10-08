package com.playlistmaker.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListEditorVm
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListEditorFragment : Fragment() {
    private val vm: FragmentPlayListEditorVm by viewModel()
    private var param = -1L

    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!


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

        lifecycleScope.launch {
            vm.screenState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                binding.txtInfo.text = it
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Достаем информацию о листе через базу данных, используя
        // переданный в аргументах id листа
        vm.evaluatePlayList(param)

    }

    companion object {
        const val PLAYLIST_ID_ARG = "ARG"
    }
}