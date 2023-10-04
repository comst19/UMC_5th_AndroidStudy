package com.comst.flocloneapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ActivitySongBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private val mainViewModel : MainViewModel by viewModels()
    private var job : Job? = null


    var repeat = false
    var shuffle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song)
        val musicTitle = intent.getStringExtra("musicTitle")
        val musicSinger = intent.getStringExtra("musicSinger")

        val albumIncludeMusic = AlbumIncludeMusic(0, musicTitle!!, false, musicSinger!!)
        mainViewModel.updateMiniPlayerUI(albumIncludeMusic)
        initView()
        setObserve()
    }

    private fun initView(){

        with(binding){
            vm = mainViewModel
            lifecycleOwner = this@SongActivity

            binding.toolbarLayout.setPadding(0,getStatusBarHeight(this@SongActivity)/2, 0, 0)

            songDownIb.setOnClickListener {
                val intent = Intent(this@SongActivity, MainActivity::class.java)

                intent.putExtra("musicTitle", songMusicTitleTv.text.toString())
                intent.putExtra("musicSinger", songSingerNameTv.text.toString())
                setResult(100, intent)
                finish()
            }

            songMiniplayerIv.setOnClickListener {
                mainViewModel.musicPlay.value = !mainViewModel.musicPlay.value!!
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
                    mainViewModel.setMusicTime(songProgressSb.progress)
                    updateTimerText(mainViewModel.musicTime.value!!)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mainViewModel.setMusicTime(songProgressSb.progress)
                    updateTimerText(mainViewModel.musicTime.value!!)
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
                mainViewModel.setMusicTime((songProgressSb.progress + 5).coerceAtMost(songProgressSb.max))

            }
            songPreviousIv.setOnClickListener {
                mainViewModel.setMusicTime((songProgressSb.progress - 5).coerceAtLeast(0))
            }
        }
    }

    private fun setObserve(){

        mainViewModel.musicTime.observe(this){
            if (it == 60){
                job?.cancel()
                mainViewModel.musicPlay.value = false
            }
            updateTimerText(mainViewModel.musicTime.value!!)
        }

        mainViewModel.musicPlay.observe(this){
            if (it){
                binding.songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                    R.drawable.btn_miniplay_pause
                ))
                job = CoroutineScope(Dispatchers.Main).launch {
                    while (it && binding.songProgressSb.progress < binding.songProgressSb.max){
                        delay(1000)
                        mainViewModel.setMusicTime(mainViewModel.musicTime.value?.plus(1) ?: 0)
                    }
                }
            }else{
                binding.songMiniplayerIv.setImageDrawable(AppCompatResources.getDrawable(this@SongActivity,
                    R.drawable.btn_miniplayer_play
                ))
                job?.cancel()
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