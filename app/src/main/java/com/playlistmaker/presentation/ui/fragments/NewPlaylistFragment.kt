package com.playlistmaker.presentation.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.activities.ActivityPlayerB
import com.playlistmaker.presentation.ui.activities.MainActivity
import com.playlistmaker.presentation.ui.viewmodel.FragmentNewPlayListVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

const val PLAYLIST_COVER = "PLAYLIST_COVERS"

class NewPlaylistFragment : Fragment() {

    private val vm: FragmentNewPlayListVm by viewModel()

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
            (requireActivity() as AlertMessaging).showSnackBar("GOOD")
            pickImageFromGallery()
        } else {
            (requireActivity() as AlertMessaging).showSnackBar("NO PERMISSIONS")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)


        vm.selectedImage.observe(viewLifecycleOwner) { setImageAsCover(it) }

        vm.errorMsg.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        vm.exitTrigger.observe(viewLifecycleOwner) {
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

    private fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri == null) return
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVER
        )

        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

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
        // Функция считывает все обложки, сохраненные внутри каталога приложения
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVER
        )

        if (filePath.exists()) {
            val logString = StringBuilder()
            filePath.listFiles()?.forEachIndexed { index, element ->
                logString.append("($index)-$element\n")
            }
            Snackbar.make(binding.imgAddPhoto, logString.toString(), Snackbar.LENGTH_INDEFINITE)
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
            checkAndAskPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.btnCreatePlaylist.setOnClickListener {
            saveImageToPrivateStorage(vm.selectedImage.value)
            vm.savePlayListToDB()
        }

        binding.btnCheckBase.setOnClickListener { directoryCheck() }

        val backCallback = object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                exitWithDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

    }

    private fun checkAndAskPermission(permission: String) {
        when {
            haveRequiredPermission(permission) -> {
                // You can use the API that requires the permission.
                pickImageFromGallery()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                (requireActivity() as AlertMessaging).showSnackBar("Необходимо разрешение на доступ к изображениям на телефоне!")
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO
                        )
                    )
                } else {
                    requestPermissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            }
        }
    }

    private fun haveRequiredPermission(permissionToCheck: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permissionToCheck
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickImageFromGallery() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(requireContext())) {
            pickImageContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun exit() {
        when (requireActivity()) {
            is MainActivity -> (requireActivity() as MainActivity).navigateBack()
            is ActivityPlayerB -> parentFragmentManager.popBackStack()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("LOG_TAG", "DESTROY PLAYLIST")
        parentFragmentManager.setFragmentResult(FRAGMENT_NEW_PLAY_LIST_REQUEST_KEY, Bundle())

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("LOG", "onDetachedFromWindow NEW PLAYLIST")
    }

    companion object {
        const val FRAGMENT_NEW_PLAY_LIST_REQUEST_KEY = "NEW_PLAYLIST_DESTROY"
        private const val REQUEST_PERMISSION_CODE = 101
    }

}