package com.playlistmaker.presentation.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.activities.ActivityPlayerB
import com.playlistmaker.presentation.ui.recycleradapter.SearchTrackAdapter
import com.playlistmaker.presentation.ui.recycleradapter.Syntactic
import com.playlistmaker.presentation.ui.viewmodel.FragmentPlayListViewerVm
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListEditorFragment : PlayListViewerFragment() {

}