package com.playlistmaker.presentation.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.playlistmaker.presentation.ui.activities.MainActivity
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

        // Перехватываем нажатие назад
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback = object :
            OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                exitWithDialog()
            }
        })

        vm.btnCreateEnable.observe(viewLifecycleOwner) {
            binding.btnCreatePlaylist.isEnabled = it
        }

        setBehaviour()

        return binding.root
    }

    private fun exitWithDialog() {
        if (vm.nothingChanged()) {
            (requireActivity() as MainActivity).navigateBack()

        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")

                .setNegativeButton("Отмена") { dialog, which ->
                    // выходим из окна без сохранения
                    //finish()
                }.setPositiveButton("Завершить") { dialog, which ->
                    // сохраняем изменения и выходим
                    // save()
                    (requireActivity() as MainActivity).navigateBack()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setBehaviour() {
        binding.txtPlaylistName.doAfterTextChanged {
            vm.changePlayListName(it.toString())
        }

        binding.btnBack.setOnClickListener { exitWithDialog() }

        binding.layoutAddPhoto.setOnClickListener {
            Snackbar.make(binding.btnBack,"text",Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}