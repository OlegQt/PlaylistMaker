package com.playlistmaker.presentation.ui.fragments

 import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.R
 import com.playlistmaker.presentation.models.AlertMessaging
 import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListEditorVm
 import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListEditorFragment : Fragment() {
        val vm: FragmentPlayListEditorVm by viewModel()
        var param = -1L
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                param = it.getLong(PLAYLIST_ID_ARG)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            //vm = ViewModelProvider(this)[FragmentPlayListEditorVm::class.java]

            vm.errorMsg.observe(viewLifecycleOwner){
                (requireActivity() as AlertMessaging).showSnackBar(it)
            }

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_playlist_editor, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //vm.setError("PlayListNUM = $param")
        vm.evaluatePlayList(param)
    }

        companion object{
            const val PLAYLIST_ID_ARG = "ARG"
        }
    }