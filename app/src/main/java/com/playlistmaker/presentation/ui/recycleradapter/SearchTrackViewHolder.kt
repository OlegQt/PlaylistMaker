package com.playlistmaker.presentation.ui.recycleradapter

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.R
import com.playlistmaker.domain.models.MusicTrack
import java.text.SimpleDateFormat
import java.util.Locale

class SearchTrackViewHolder(
    private val item: View,
    listener: SearchTrackAdapter.OnTrackClickListener
) : RecyclerView.ViewHolder(item) {
    private var txtTrackName: TextView = item.findViewById(R.id.track_name)
    private var txtArtistName: TextView = item.findViewById(R.id.artist_name)
    private var txtTrackTime: TextView = item.findViewById(R.id.track_time)
    private var imgArtCover: ImageView = item.findViewById(R.id.art_work)


    init {
        item.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Обработка нажатия на элемент
                // Toast.makeText(it?.context,"Push",Toast.LENGTH_SHORT).show()
                listener.onTrackClick(position)
            }
        }
    }

    private fun getFullDurationFromLong(duration: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
    }

    fun getRootView():View = item

    fun bind(musTrack: MusicTrack) {
        txtTrackName.text = musTrack.trackName
        txtArtistName.text = musTrack.artistName
        txtTrackTime.text = getFullDurationFromLong(musTrack.trackTimeMillis)

        val dens = Resources.getSystem().displayMetrics.density
        val picCornerRad = 4 * dens
        Log.d("LOGTest", "Pixel density = $dens")
        Glide
            .with(item.context)
            .load(musTrack.artworkUrl100)
            .placeholder(R.drawable.placeholder_no_track)
            .centerCrop()
            .transform(RoundedCorners(picCornerRad.toInt())) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(imgArtCover)
    }

}