package com.playlistmaker.logic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.playlistmaker.R
import com.playlistmaker.domain.models.MusicTrack

class SearchTrackAdapter(private val trackList: ArrayList<MusicTrack>, private var onTrackClickListener:OnTrackClickListener) :
    Adapter<SearchTrackViewHolder>() {

    fun interface OnTrackClickListener {
        fun onTrackClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.track_item,
                parent,
                false
            )
        return SearchTrackViewHolder(item,onTrackClickListener)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount() =trackList.size

    fun getItem(position:Int): MusicTrack {
        return trackList[position]
    }
}