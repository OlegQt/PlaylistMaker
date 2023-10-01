package com.playlistmaker.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.databinding.PlaylistShortItemBinding
import com.playlistmaker.domain.models.PlayList

class PlayListAdapter(
    private val playListDB: List<PlayList>,
    private val type: RecyclerType,
    private val clickListener: OnClickListener?
) :
    Adapter<PlayListVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListVH {
        return when (type) {
            RecyclerType.SMALL -> {
                val binding: PlaylistShortItemBinding =
                    PlaylistShortItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                PlayListViewHolderSmall(binding)
            }

            RecyclerType.LARGE -> {
                val binding: PlaylistItemBinding =
                    PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PlayListViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = playListDB.size

    override fun onBindViewHolder(holder: PlayListVH, position: Int) {
        holder.bind(playList = playListDB[position])
        holder.getRootView().setOnClickListener { clickListener?.onClick(position) }
    }

    fun interface OnClickListener {
        fun onClick(position: Int)
    }

    enum class RecyclerType() {
        SMALL,
        LARGE
    }

}