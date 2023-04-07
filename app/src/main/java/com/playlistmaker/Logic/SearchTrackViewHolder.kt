package com.playlistmaker.Logic

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R

class SearchTrackViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
    private var txtTrackName: TextView = item.findViewById(R.id.track_name)
    private var txtArtistName: TextView = item.findViewById(R.id.artist_name)
    private var txtTrackTime: TextView = item.findViewById(R.id.track_time)
    private var imgArtCover: ImageView = item.findViewById(R.id.art_work)


    fun bind(musTrack: Track) {
        txtTrackName.text = musTrack.trackName
        txtArtistName.text = musTrack.artistName
        txtTrackTime.text = musTrack.trackTime


        val dens = Resources.getSystem().displayMetrics.density
        val picCornerRad = 4*dens
        Log.d("LOGTest","Pixel density = $dens")
        Glide
            .with(item.context)
            .load(musTrack.artworkUrl100)
            .placeholder(R.drawable.search_pic)
            .centerCrop()
            .transform(RoundedCorners(picCornerRad.toInt())) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(imgArtCover)
    }
}