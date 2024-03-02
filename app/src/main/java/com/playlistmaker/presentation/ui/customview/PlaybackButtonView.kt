package com.playlistmaker.presentation.ui.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.playlistmaker.R

/*
class PlaybackButtonView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

}*/

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var currentButtonState: PlaybackButtonState = PlaybackButtonState.IS_PLAYING
    private var imagePause: Bitmap? = null
    private var imagePlay: Bitmap? = null
    private val paint = Paint().apply { isAntiAlias = true }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val str = getString(R.styleable.PlaybackButtonView_start_txt) ?: "null start txt"

                imagePlay =
                    getDrawable(R.styleable.PlaybackButtonView_play_image_res_id)?.toBitmap()

                imagePause =
                    getDrawable(R.styleable.PlaybackButtonView_pause_image_res_id)?.toBitmap()


            } finally {
                recycle()
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_UP -> {
                changeState(newState = getOppositeState())
                performClick()
                true
            }

            MotionEvent.ACTION_DOWN -> true
            else -> true
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val backgroundImage =
            when (currentButtonState) {
                PlaybackButtonState.IS_PLAYING -> imagePlay
                PlaybackButtonState.IS_PAUSED -> imagePause
            }

        backgroundImage?.let {
            canvas?.drawBitmap(it, 0f, 0f, paint)
        }
    }

    fun changeState(newState: PlaybackButtonState) {
        currentButtonState = newState

        invalidate()
    }

    private fun getOppositeState(): PlaybackButtonState {
        return when (currentButtonState) {
            PlaybackButtonState.IS_PLAYING -> PlaybackButtonState.IS_PAUSED
            PlaybackButtonState.IS_PAUSED -> PlaybackButtonState.IS_PLAYING
        }
    }

    companion object {
        enum class PlaybackButtonState() {
            IS_PLAYING, IS_PAUSED
        }
    }

}