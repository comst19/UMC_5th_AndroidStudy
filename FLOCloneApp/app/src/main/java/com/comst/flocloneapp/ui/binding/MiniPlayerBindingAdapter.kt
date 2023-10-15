package com.comst.flocloneapp.ui.binding

import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.comst.flocloneapp.R

@BindingAdapter("isPlaying")
fun setPlayOrPauseImg(view : ImageView, isPlaying : Boolean){
    val drawableResId = if (isPlaying) {
        R.drawable.btn_miniplay_pause
    } else {
        R.drawable.btn_miniplayer_play
    }
    val drawable = AppCompatResources.getDrawable(view.context, drawableResId)
    view.setImageDrawable(drawable)
}


@BindingAdapter("miniPlayerSeekBar")
fun setProgress(seekBar: SeekBar, progress: Int) {
    seekBar.progress = progress
}