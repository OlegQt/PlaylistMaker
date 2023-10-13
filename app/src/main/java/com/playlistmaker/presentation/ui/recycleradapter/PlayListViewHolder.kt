package com.playlistmaker.presentation.ui.recycleradapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.playlistmaker.R
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.databinding.PlaylistShortItemBinding
import com.playlistmaker.domain.models.PlayList


abstract class PlayListVH(itemView:View):ViewHolder(itemView){
    abstract fun bind(playList: PlayList)
    abstract fun getRootView():ViewGroup
    fun getTrackEnding(number: Int): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number трек"
            number % 10 in (2..4) && (number % 100 < 10) -> "$number трека"
            number % 10 in (2..4) && (number % 100 >20) -> "$number трека"
            else -> "$number треков"
        }
    }
}
class PlayListViewHolder(private val binding: PlaylistItemBinding) : PlayListVH(binding.root) {
    override fun bind(playList: PlayList) {
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = Syntactic.getTrackEnding(playList.quantity)

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(160, 160)
            .into(binding.imgPlaylistCover)

    }

    override fun getRootView(): ViewGroup =binding.root
}

class PlayListViewHolderSmall(private val binding: PlaylistShortItemBinding) : PlayListVH(binding.root) {
    override fun bind(playList: PlayList) {
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = getTrackEnding(playList.quantity)

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(45, 45)
            .into(binding.imgPlaylistCover)
    }

    override fun getRootView():ViewGroup= binding.rootLayout
}