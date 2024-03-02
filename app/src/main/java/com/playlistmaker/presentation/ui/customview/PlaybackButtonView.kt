package com.playlistmaker.presentation.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
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

    private var currentButtonState: PlaybackButtonState = PlaybackButtonState.IS_PAUSED

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.black))

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val str = getString(R.styleable.PlaybackButtonView_start_txt) ?: "null start txt"
            } finally {
                recycle()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            return when (it.action) {
                MotionEvent.ACTION_UP -> {
                    changeState(newState = getOppositeState())
                    true
                }

                MotionEvent.ACTION_DOWN -> true
                else -> true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun changeState(newState: PlaybackButtonState) {
        currentButtonState = newState

        val msgString = when (currentButtonState) {
            PlaybackButtonState.IS_PLAYING -> "IS_PLAYING".also {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.Text_Red
                    )
                )
            }

            PlaybackButtonState.IS_PAUSED -> "IS_PAUSED".also {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.Text_Grey
                    )
                )
            }
        }

        Toast.makeText(context, "state: $msgString", Toast.LENGTH_SHORT).show()
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