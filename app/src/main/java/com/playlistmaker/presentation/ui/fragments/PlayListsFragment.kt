package com.playlistmaker.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.playlistmaker.R


class PlayListsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    companion object{
        // На данном этапе не требуется передавать никаких параметров, однако в задании
        // жестко задано создавать фрагмент через instance
        fun newInstance()=PlayListsFragment()
    }
}