package com.comst.flocloneapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ActivitySongBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private val miniPlayerViewModel : MiniPlayerViewModel by viewModels()

    var repeat = false
    var shuffle = false

    override fun onBackPressed() {
        goMainActivity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song)

        val musicTitle = intent.getStringExtra("musicTitle")
        val musicSinger = intent.getStringExtra("musicSinger")
        val musicTime = intent.getIntExtra("musicTime", 0)
        val musicPlay = intent.getBooleanExtra("musicPlay", false)

        val albumIncludeMusic = AlbumIncludeMusic(0, musicTitle!!, false, musicSinger!!)

        miniPlayerViewModel.updateMiniPlayerUI(albumIncludeMusic)

        miniPlayerViewModel.setMusicTime(musicTime)
        updateTimerText(musicTime)
        miniPlayerViewModel.musicPlay.value = musicPlay

        initView()
        setObserve()
    }

    private fun initView(){

        with(binding){
            vm = miniPlayerViewModel
            lifecycleOwner = this@SongActivity

            binding.toolbarLayout.setPadding(0,getStatusBarHeight(this@SongActivity)/2, 0, 0)

            songDownIb.setOnClickListener {
                goMainActivity()
            }

            songMiniplayerIv.setOnClickListener {
                miniPlayerViewModel.musicPlay.value = !miniPlayerViewModel.musicPlay.value!!
                miniPlayerViewModel.musicPlayOrPause()
            }

            songProgressSb.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser){
                        updateTimerText(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    miniPlayerViewModel.setMusicTime(songProgressSb.progress)
                    updateTimerText(miniPlayerViewModel.musicTime.value!!)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    miniPlayerViewModel.setMusicTime(songProgressSb.progress)
                    updateTimerText(miniPlayerViewModel.musicTime.value!!)
                }

            })

            songRepeatIv.setOnClickListener {
                repeat = !repeat
                if (repeat){
                    songRepeatIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                        R.drawable.nugu_btn_repeat_inactive_blue
                    ))
                }else{
                    songRepeatIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                        R.drawable.nugu_btn_repeat_inactive
                    ))
                }
            }

            songRandomIv.setOnClickListener {
                shuffle = !shuffle
                if (shuffle){
                    songRandomIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                        R.drawable.nugu_btn_random_inactive_blue
                    ))
                }else{
                    songRandomIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                        R.drawable.nugu_btn_random_inactive
                    ))
                }
            }

            songNextIv.setOnClickListener {
                miniPlayerViewModel.setMusicTime((songProgressSb.progress + 5).coerceAtMost(songProgressSb.max))

            }
            songPreviousIv.setOnClickListener {
                miniPlayerViewModel.setMusicTime((songProgressSb.progress - 5).coerceAtLeast(0))
            }
        }
    }

    private fun goMainActivity(){
        val intent = Intent(this@SongActivity, MainActivity::class.java)

        intent.putExtra("musicTitle", miniPlayerViewModel.musicPlayTitle.value)
        intent.putExtra("musicSinger", miniPlayerViewModel.musicPlayArtist.value)
        intent.putExtra("musicTime", miniPlayerViewModel.musicTime.value)
        intent.putExtra("musicPlay", miniPlayerViewModel.musicPlay.value)

        setResult(100, intent)
        finish()
    }

    private fun setObserve(){

        miniPlayerViewModel.musicTime.observe(this){
            miniPlayerViewModel.checkMusicTimeAndStop()
            updateTimerText(miniPlayerViewModel.musicTime.value!!)
        }

        miniPlayerViewModel.isMusicTimeOver.observe(this) { isOver ->
            if(isOver) {
                MusicPlayServiceUtil.stopService(this)
            }
        }


        miniPlayerViewModel.musicPlay.observe(this){
            miniPlayerViewModel.musicPlayOrPause()
            if (it){
                MusicPlayServiceUtil.startService(this@SongActivity)
            }else{
                MusicPlayServiceUtil.pauseService(this@SongActivity)
            }
        }
    }

    private fun updateTimerText(seconds : Int){
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val seconds = seconds % 60
        binding.songStartTimeTv.text = String.format("%02d:%02d", minutes, seconds)
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