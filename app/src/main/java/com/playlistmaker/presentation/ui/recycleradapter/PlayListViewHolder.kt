package com.playlistmaker.presentation.ui.recycleradapter

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.playlistmaker.R
import com.playlistmaker.databinding.PlaylistItemBinding
import com.playlistmaker.databinding.PlaylistShortItemBinding
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.presentation.ui.fragments.NewPlaylistFragment

const val COVER_BODY_RADIUS = 4.0F
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

        // Расчет радиуса скругления
        val picCornerRad = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            COVER_BODY_RADIUS,
            Resources.getSystem().displayMetrics
        )

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(160, 160)
            .signature(ObjectKey(System.currentTimeMillis()))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(picCornerRad.toInt())))
            .into(binding.imgPlaylistCover)

    }

    override fun getRootView(): ViewGroup =binding.root
}

class PlayListViewHolderSmall(private val binding: PlaylistShortItemBinding) : PlayListVH(binding.root) {
    override fun bind(playList: PlayList) {
        binding.txtPlaylistName.text = playList.name
        binding.txtAmount.text = getTrackEnding(playList.quantity)

        // Расчет радиуса скругления
        val picCornerRad = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            COVER_BODY_RADIUS/2,
            Resources.getSystem().displayMetrics
        )

        Glide
            .with(binding.root)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder_no_track)
            .override(45, 45)
            .signature(ObjectKey(System.currentTimeMillis()))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(picCornerRad.toInt())))
            .into(binding.imgPlaylistCover)
    }

    override fun getRootView():ViewGroup= binding.rootLayout
}