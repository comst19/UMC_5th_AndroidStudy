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
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private lateinit var miniPlayerViewModel : MiniPlayerViewModel

    var repeat = false
    var shuffle = false

    override fun onBackPressed() {
        goMainActivity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song)
        miniPlayerViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(MiniPlayerViewModel::class.java)

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

        val musicTitle = miniPlayerViewModel.musicPlayTitle.value
        val musicSinger = miniPlayerViewModel.musicPlayArtist.value
        val musicTime = miniPlayerViewModel.musicTime.value
        val musicPlay = miniPlayerViewModel.musicPlay.value

        intent.putExtra("musicTitle", musicTitle)
        intent.putExtra("musicSinger", musicSinger)
        intent.putExtra("musicTime", musicTime)
        intent.putExtra("musicPlay", musicPlay)

        Log.d("보내는 값", "Sending data: title=$musicTitle, singer=$musicSinger, time=$musicTime, play=$musicPlay")

        setResult(100, intent)
        finish()
    }

    private fun setObserve(){

        miniPlayerViewModel.musicTime.observe(this){
            if (it == 60){
                miniPlayerViewModel.job?.cancel()
                miniPlayerViewModel.musicPlay.value = false
            }
            updateTimerText(miniPlayerViewModel.musicTime.value!!)
        }


        miniPlayerViewModel.musicPlay.observe(this){
            if (it){
                binding.songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                    R.drawable.btn_miniplay_pause
                ))
                miniPlayerViewModel.job = CoroutineScope(Dispatchers.Main).launch {
                    while (it && binding.songProgressSb.progress < binding.songProgressSb.max){
                        delay(1000)
                        miniPlayerViewModel.setMusicTime(miniPlayerViewModel.musicTime.value?.plus(1) ?: 0)
                    }
                }
            }else{
                binding.songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                    R.drawable.btn_miniplayer_play
                ))
                miniPlayerViewModel.job?.cancel()
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