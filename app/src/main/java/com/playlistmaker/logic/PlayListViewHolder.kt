package com.playlistmaker.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.domain.models.PlayList

class PlayListViewHolder(private val binding:PlaylistItemBinding):ViewHolder(binding.root) {

    fun bind(playList:PlayList){
        binding.txtPlaylistName.text = playList.name

        Glide
            .with(binding.imgPlaylistCover)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .centerCrop()
            .transform(RoundedCorners(20)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(binding.imgPlaylistCover)
    }
}