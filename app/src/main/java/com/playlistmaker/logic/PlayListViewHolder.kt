package com.playlistmaker.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.playlistmaker.R
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.domain.models.PlayList

class PlayListViewHolder(private val binding:PlaylistItemBinding):ViewHolder(binding.root) {

    fun bind(playList:PlayList){
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = "0 треков"

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .transform(RoundedCorners(8)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .override(160,160)
            .centerCrop()
            .into(binding.imgPlaylistCover)
    }
}