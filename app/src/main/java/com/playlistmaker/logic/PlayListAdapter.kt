package com.playlistmaker.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.domain.models.PlayList

class PlayListAdapter(private val playListDB: List<PlayList>) : Adapter<PlayListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val binding: PlaylistItemBinding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayListViewHolder(binding)
    }

    override fun getItemCount(): Int = playListDB.size

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playList = playListDB[position])
    }

}