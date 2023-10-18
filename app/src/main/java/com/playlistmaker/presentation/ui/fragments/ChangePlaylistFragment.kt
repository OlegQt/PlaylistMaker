package com.playlistmaker.presentation.ui.fragments

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListEditorVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ChangePlaylistFragment : NewPlaylistFragment() {
    override val vm: FragmentPlayListEditorVm by viewModel()
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

        with(binding) {
            topAppBar.title = "Редактировать"

            btnCreatePlaylist.text = "Сохранить"

            txtPlaylistDescription.doAfterTextChanged {
                vm.changeDescription(newDescription = it.toString())
            }

            txtPlaylistName.doAfterTextChanged {
                vm.changePlayListName(it.toString())
            }

            topAppBar.setNavigationOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            layoutAddPhoto.setOnClickListener {
                checkAndAskPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            btnCreatePlaylist.setOnClickListener {
                saveImageToPrivateStorage(vm.selectedImage.value)
                vm.updatePlayListInfo()
            }
        }

        with(vm) {
            errorMsg.observe(viewLifecycleOwner) {
                (requireActivity() as AlertMessaging).showSnackBar(it)
            }

            btnCreateEnable.observe(viewLifecycleOwner) {
                binding.btnCreatePlaylist.isEnabled = it

                if (it) binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.yp_blue))
                else binding.btnCreatePlaylist.setBackgroundColor(requireActivity().getColor(R.color.Text_Grey))

            }

            playListOpened.observe(viewLifecycleOwner) {
                it?.let {
                    setImageAsCover(Uri.fromFile(File(it.cover)))
                    binding.txtPlaylistName.setText(it.name)
                    binding.txtPlaylistDescription.setText(it.description)
                }
            }

            vm.selectedImage.observe(viewLifecycleOwner){
                setImageAsCover(it)
            }
        }

        return binding.root
    }

    private fun setImageAsCover(uri: Uri) {
        // Изменяем layout картинки
        with(binding.imgAddPhoto) {
            // Установите параметры ширины и высоты на wrap_content
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            // Обновите ImageView, чтобы применить изменения
            requestLayout()
        }

        // Загружаем изображение
        Glide.with(binding.root.context)
            .load(uri)
            .placeholder(R.drawable.no_track_found)
            .into(binding.imgAddPhoto)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM_1 = "param_id_playlist"

        fun setArg(playListId: Long) = PlayListEditorFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_PARAM_1, playListId)
            }
        }
    }
}