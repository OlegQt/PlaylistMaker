package com.playlistmaker.presentation.ui.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.viewmodel.FragmentChangePlaylistVm
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChangePlaylistFragment : NewPlaylistFragment() {

    override val vm:FragmentChangePlaylistVm by viewModel()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var pickImageContent =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { vm.handlePickedImage(it) }
        }

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.READ_MEDIA_VIDEO] == true && it[Manifest.permission.READ_MEDIA_IMAGES] == true) {
            //(requireActivity() as AlertMessaging).showSnackBar("GOOD")
            //pickImageFromGallery()
        } else {
            (requireActivity() as AlertMessaging).showSnackBar("Предоставьте приложению разрешение на доступ к изображениям в настройках")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val param = it.getLong(ARG_PLAYLIST_ID)
            Log.e("LOG","param for change $param")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        vm.errorMsg.observe(viewLifecycleOwner){
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        vm.btnCreateEnable.observe(viewLifecycleOwner) {
            binding.btnCreatePlaylist.isEnabled = it

            if (it) binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.yp_blue))
            else binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.Text_Grey))

        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title ="Редактировать"

        binding.btnCreatePlaylist.text ="Сохранить"

        binding.txtPlaylistName.doAfterTextChanged {
            vm.changePlayListName(it.toString())
        }


    }





    override fun onDestroy() {
        super.onDestroy()
        Log.e("LOG_TAG", "DESTROY PLAYLIST")
        parentFragmentManager.setFragmentResult(FRAGMENT_NEW_PLAY_LIST_REQUEST_KEY, Bundle())
        _binding = null
    }


    companion object {
        fun changeInstance(playListId:Long) = ChangePlaylistFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_PLAYLIST_ID,playListId)
            }
        }
    }

}