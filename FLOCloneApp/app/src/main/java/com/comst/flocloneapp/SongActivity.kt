package com.comst.flocloneapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comst.flocloneapp.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){

        with(binding){

            binding.toolbarLayout.setPadding(0,getStatusBarHeight(this@SongActivity)/2, 0, 0)

            songDownIb.setOnClickListener {
                val intent = Intent(this@SongActivity, MainActivity::class.java)
                intent.putExtra("musicTitle", songMusicTitleTv.text.toString())
                intent.putExtra("musicSinger", songSingerNameTv.text.toString())
                setResult(100, intent)
                finish()
            }
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
}