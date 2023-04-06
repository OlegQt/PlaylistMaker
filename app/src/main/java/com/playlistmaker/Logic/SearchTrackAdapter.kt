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
        txtArtistName = item.findViewById(R.id.artist_name)
        txtTrackTime = item.findViewById(R.id.track_time)
        imgArtCover = item.findViewById(R.id.art_work)
    }

    fun bind(musTrack: Track) {
        txtTrackName?.text = musTrack.trackName
        txtArtistName?.text = musTrack.artistName
        txtTrackTime?.text = musTrack.trackTime

        if (imgArtCover != null) Glide
            .with(item.context)
            .load(musTrack.artworkUrl100)
            .placeholder(R.drawable.search_pic)
            .centerCrop()
            .transform(RoundedCorners(2)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(imgArtCover!!)
    }
}