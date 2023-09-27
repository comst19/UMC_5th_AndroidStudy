package com.comst.flocloneapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.comst.flocloneapp.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding

    var musicPlay = false
    var repeat = false
    var shuffle = false

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

            songMiniplayerIv.setOnClickListener {
                musicPlay = !musicPlay
                if (musicPlay){
                    songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.btn_miniplay_pause))
                }else{
                    songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.btn_miniplayer_play))
                }
            }

            songRepeatIv.setOnClickListener {
                repeat = !repeat
                if (repeat){
                    songRepeatIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.nugu_btn_repeat_inactive_blue))
                }else{
                    songRepeatIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.nugu_btn_repeat_inactive))
                }
            }

            songRandomIv.setOnClickListener {
                shuffle = !shuffle
                if (shuffle){
                    songRandomIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.nugu_btn_random_inactive_blue))
                }else{
                    songRandomIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity, R.drawable.nugu_btn_random_inactive))
                }
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