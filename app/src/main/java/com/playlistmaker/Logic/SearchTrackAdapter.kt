package com.playlistmaker.Logic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.itunes.ItunesTrack

class SearchTrackAdapter(private val trackList: ArrayList<ItunesTrack>,private var onTrackClickListener:OnTrackClickListener) :
    Adapter<SearchTrackViewHolder>() {

    interface OnTrackClickListener {
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

    fun getItem(position:Int):ItunesTrack{
        return trackList[position]
    }
}