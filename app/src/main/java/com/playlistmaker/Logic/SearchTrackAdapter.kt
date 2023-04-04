package com.playlistmaker.Logic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.playlistmaker.R

class SearchTrackAdapter(private val trackList: ArrayList<Track>) :
    Adapter<SearchTrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.track_item,
                parent,
                false
            )
        return SearchTrackViewHolder(item)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

class SearchTrackViewHolder(private val item: View) : ViewHolder(item) {
    private var txtTrackName: TextView? = null
    private var txtArtistName: TextView? = null
    private var txtTrackTime: TextView? = null
    private var imgArtCover: ImageView? = null
    init {
        txtTrackName = item.findViewById(R.id.track_name)
        txtArtistName= item.findViewById(R.id.artist_name)
        txtTrackTime =item.findViewById(R.id.track_time)
    }

    fun bind(musTrack: Track) {
        txtTrackName?.text = musTrack.trackName
        txtArtistName?.text = musTrack.artistName
        txtTrackTime?.text=musTrack.trackTime
    }

}