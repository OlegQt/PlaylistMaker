package com.playlistmaker.logic

import android.text.Layout
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.databinding.PlaylistShortItemBinding
import com.playlistmaker.domain.models.PlayList


abstract class PlayListVH(itemView:View):ViewHolder(itemView){
    abstract fun bind(playList: PlayList)
    abstract fun getRootView():ViewGroup
}
class PlayListViewHolder(private val binding: PlaylistItemBinding) : PlayListVH(binding.root) {
    override fun bind(playList: PlayList) {
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = "${playList.quantity} треков"

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(160, 160)

            .transform(RoundedCorners(22)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .centerCrop()
            .into(binding.imgPlaylistCover)
    }

    override fun getRootView(): ViewGroup =binding.root
}

class PlayListViewHolderSmall(private val binding: PlaylistShortItemBinding) : PlayListVH(binding.root) {
    override fun bind(playList: PlayList) {
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = "${playList.quantity} треков"

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(45, 45)

            .transform(RoundedCorners(40)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .centerCrop()
            .into(binding.imgPlaylistCover)
    }

    override fun getRootView():ViewGroup= binding.rootLayout
}