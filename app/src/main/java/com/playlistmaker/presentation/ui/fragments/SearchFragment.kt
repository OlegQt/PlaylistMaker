package com.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.playlistmaker.databinding.FragmentSearchBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.logic.SearchTrackAdapter
import com.playlistmaker.presentation.models.ActivitySearchState
import com.playlistmaker.presentation.ui.activities.ActivityPlayer
import com.playlistmaker.presentation.ui.viewmodel.FragmentSearchVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val vm: FragmentSearchVm by viewModel()

    // KOIN viewModel


    // Адаптеры для отображения найденных треков и истории просмотра треков
    private lateinit var musicAdapter: SearchTrackAdapter
    private lateinit var musicSearchHistoryAdapter: SearchTrackAdapter

    // List of tracks
    private val musicList: ArrayList<MusicTrack> = ArrayList()
    private val musicSearchHistoryList: ArrayList<MusicTrack> = ArrayList()

    // Функция для перехода на экран плеера
    private fun startPlayerActivity(musicTrackToPlay: MusicTrack) {
        val intentPlayerActivity = Intent(requireContext(), ActivityPlayer::class.java)
        intentPlayerActivity.putExtra("track", musicTrackToPlay)
        startActivity(intentPlayerActivity)
    }

    private fun clsButtonVisibility() {
        val visibility = binding.txtSearch.text.isNotEmpty()
        binding.clsSearch.visibility = when (visibility) {
            true -> View.VISIBLE
            false -> View.GONE
        }

    }

    // Функции для изменения состояний экрана
    private fun stateNothingFound() {
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE

        binding.imgStub.setImageResource(R.drawable.nothing_found) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.nothing_found)
    }

    private fun stateInitial() {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE
        //showAlertDialog("InitialState")
    }

    private fun stateInternetTroubles() {
        binding.stubLayout.visibility = View.VISIBLE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.btnReload.visibility = View.VISIBLE

        binding.imgStub.setImageResource(R.drawable.connection_troubles) // Set Proper image from drawable
        binding.txtStubMainError.text = getString(R.string.connection_troubles)
            .plus("\n")
            .plus(getString(R.string.loading_fail))

    }

    private fun stateLoading() {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressLoading.visibility = View.GONE
        binding.progressLoading.visibility = View.VISIBLE
        binding.searchRecycleView.visibility = View.GONE
    }

    // Отображение истории поиска музыкальных композиций
    private fun stateShowMusicSearchHistory(musicHistory: ArrayList<MusicTrack>) {
        binding.stubLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.VISIBLE
        binding.progressLoading.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE

        this.musicSearchHistoryList.clear()
        this.musicSearchHistoryList.addAll(musicHistory)
        this.musicSearchHistoryAdapter.notifyDataSetChanged()

    }

    // Отображение найденного списка музыкальных композиций
    private fun updateMusicList(music: ArrayList<MusicTrack>) {
        this.musicList.clear()
        this.musicList.addAll(music)
        this.musicAdapter.notifyDataSetChanged()

        this.stateInitial()
        binding.searchRecycleView.visibility = View.VISIBLE
    }

    ////////////////////////////////////////////////////////
    // Блок переопределённых функций
    ////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        vm.getSearchScreenState.observe(viewLifecycleOwner) { this.render(it) }

        vm.getStartPlayerCommand.observe(viewLifecycleOwner) {
            startPlayerActivity(it)
        }

        vm.getErrorMsg().observe(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireActivity())
                .setMessage(it)
                .setTitle("Dialog")
                .setNeutralButton("OK", null)
                .show()
        }


        // Инициализация адаптера с описанием слушателя
        musicAdapter =
            SearchTrackAdapter(musicList) {
                vm.musicTrackOnClick(musicList[it])
            }
        binding.searchRecycleView.adapter = musicAdapter
        binding.searchRecycleView.layoutManager =
            LinearLayoutManager(binding.searchRecycleView.context)

        musicSearchHistoryAdapter = SearchTrackAdapter(musicSearchHistoryList) {
            vm.onHistoryTrackListClick(musicSearchHistoryList[it])
        }
        binding.historySearchRecycleView.adapter = musicSearchHistoryAdapter
        binding.historySearchRecycleView.layoutManager =
            LinearLayoutManager(binding.searchRecycleView.context)

        binding.txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                vm.searchLineTyping(str.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.btnClearHistory.setOnClickListener { vm.deleteMusicHistory() }
        binding.btnReload.setOnClickListener { vm.searchLineTyping(binding.txtSearch.toString()) }
        binding.clsSearch.setOnClickListener { binding.txtSearch.text.clear() }
        binding.txtSearch.setText("")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ActivitySearchState) {
        when (state) {
            is ActivitySearchState.NothingFound -> stateNothingFound()
            is ActivitySearchState.MusicSearchContent -> updateMusicList(state.music)
            is ActivitySearchState.InitialState -> stateInitial()
            is ActivitySearchState.InternetTroubles -> stateInternetTroubles()
            is ActivitySearchState.Loading -> stateLoading()
            is ActivitySearchState.HistoryMusicContent -> stateShowMusicSearchHistory(state.music)
        }
        // Проверка состояния кнопки очистки поисковой строки
        // происходит при любом State
        clsButtonVisibility()
    }
}