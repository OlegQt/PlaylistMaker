package com.playlistmaker.presentation.ui.fragments.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.playlistmaker.R
import com.playlistmaker.domain.models.MusicTrack

class SearchTrackAdapter(private val trackList: ArrayList<MusicTrack>, private var onTrackClickListener: OnTrackClickListener) :
    Adapter<SearchTrackViewHolder>() {

    var longClickListener:OnTrackLongClick? = null

    fun changeData(newData:ArrayList<MusicTrack>){

    }

    fun interface OnTrackClickListener {
        fun onTrackClick(position: Int)
    }

    fun interface OnTrackLongClick{
        fun onTrackLongClick(position: Int)
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
        holder.getRootView().setOnLongClickListener {
            longClickListener?.onTrackLongClick(position)
            true
        }
    }

    override fun getItemCount() =trackList.size
}