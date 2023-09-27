package com.playlistmaker.presentation.ui.fragments

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.ui.activities.MainActivity
import com.playlistmaker.presentation.ui.viewmodel.PlayListVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

const val PLAYLIST_COVER = "PLAYLIST_COVERS"

class NewPlaylistFragment : Fragment() {

    private val vm: PlayListVm by viewModel()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var pickImageContent =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { vm.handlePickedImage(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        vm.selectedImage.observe(viewLifecycleOwner) { setImageAsCover(it) }

        vm.errorMsg.observe(viewLifecycleOwner){
            Snackbar.make(binding.button,it,Snackbar.LENGTH_INDEFINITE).setAction("OK") { }.show()
        }

        vm.exitTrigger.observe(viewLifecycleOwner){
            exit()
        }

        vm.btnCreateEnable.observe(viewLifecycleOwner) {
            binding.btnCreatePlaylist.isEnabled = it
        }

        setBehaviour()

        return binding.root
    }

    private fun exitWithDialog() {
        if (vm.nothingChanged()) {
            exit()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { _, _ -> }
                .setPositiveButton("Завершить") { _, _ ->
                    // сохраняем изменения и выходим
                    saveImageToPrivateStorage(vm.selectedImage.value)
                    vm.savePlayListToDB()
                }.show().apply {
                    getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(
                        resources.getColor(
                            R.color.yp_blue,
                            requireActivity().theme
                        )
                    )
                    getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(
                        resources.getColor(
                            R.color.yp_blue,
                            requireActivity().theme
                        )
                    )
                }
        }

    }

    private fun setImageAsCover(uri: Uri) {
        // Изменяем layout картинки
        with(binding.imgAddPhoto) {
            // Установите параметры ширины и высоты на wrap_content
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            // Обновите ImageView, чтобы применить изменения
            requestLayout();
        }

        // Загружаем изображение
        Glide.with(binding.root.context)
            .load(uri)
            .placeholder(R.drawable.no_track_found)
            .centerCrop()
            .into(binding.imgAddPhoto)
    }

    private fun saveImageToPrivateStorage(uri: Uri?) {
        if(uri==null) return
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVER
        )

        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val name = vm.playListName
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "${vm.playListName}_cover.jpg")

        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        // Сохраняем путь к новому файлу обложки внутри нашей модели плейлиста
        vm.updatePlayListCoverLocation(file)
    }

    private fun directoryCheck() {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVER
        )

        if (filePath.exists()) {
            val fileList = filePath.list()
            val strBld = StringBuilder().apply {
                for (file in fileList) {
                    append("${file.toString()} \n")
                }
            }
            Snackbar.make(binding.button, strBld.toString(), Snackbar.LENGTH_INDEFINITE)
                .setTextMaxLines(20)
                .setAction("OK") {}
                .show()
        } else {
            Snackbar.make(binding.button, "No such directory", Snackbar.LENGTH_INDEFINITE)
                .setTextMaxLines(20)
                .setAction("OK") {}
                .show()
        }


    }

    private fun setBehaviour() {

        binding.txtPlaylistName.doAfterTextChanged {
            vm.changePlayListName(it.toString())
        }

        binding.txtPlaylistDescription.doAfterTextChanged {
            vm.changeDescription(newDescription = it.toString())
        }

        binding.btnBack.setOnClickListener { exitWithDialog() }

        binding.layoutAddPhoto.setOnClickListener {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(requireContext())) {
                pickImageContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.btnCreatePlaylist.setOnClickListener { vm.savePlayListToDB() }

        binding.button.setOnClickListener { directoryCheck() }

        binding.button.setOnLongClickListener {
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PLAYLIST_COVER
            )
            val fileList = filePath.listFiles()
            for (el in fileList) {
                el.delete()
            }
            true
        }

        // Перехватываем нажатие назад
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback = object :
            OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                exitWithDialog()
            }
        })

    }

    private fun exit() {
        (requireActivity() as MainActivity).navigateBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}