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
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.ActivitySongBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.model.SongEntity
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.util.ToastLikeOnOff
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private val miniPlayerViewModel : MiniPlayerViewModel by viewModels()
    val songs = arrayListOf<SongEntity>()
    lateinit var songDB : SongDatabase
    var nowPos = 0

    var repeat = false
    var shuffle = false

    override fun onBackPressed() {
        goMainActivity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song)

        nowPos = intent.getIntExtra("songId",0)

        initPlayList()
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

            songLikeIv.setOnClickListener {

                songs[nowPos].isLike = !songs[nowPos].isLike
                if (songs[nowPos].isLike){
                    songLikeIv.setImageResource(R.drawable.ic_my_like_on)
                    ToastLikeOnOff.createToast(this@SongActivity, "좋아요 한 곡에 담겼습니다.")?.show()
                }else{
                    songLikeIv.setImageResource(R.drawable.ic_my_like_off)
                    ToastLikeOnOff.createToast(this@SongActivity, "좋아요 한 곡이 취소되었습니다.")?.show()

                }
                CoroutineScope(Dispatchers.IO).launch{
                    songDB.SongDao().update(songs[nowPos])
                    songs.addAll(songDB.SongDao().getSongs())
                }

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
                //miniPlayerViewModel.setMusicTime((songProgressSb.progress + 5).coerceAtMost(songProgressSb.max))
                val songsSize = songs.size
                nowPos = (nowPos + 1) % songsSize
                changeSong()
                changeSongId()
            }
            songPreviousIv.setOnClickListener {
                //miniPlayerViewModel.setMusicTime((songProgressSb.progress - 5).coerceAtLeast(0))
                val songsSize = songs.size
                nowPos = if(nowPos - 1 < 0) songsSize - 1 else nowPos - 1
                changeSong()
                changeSongId()
            }
        }
    }

    private fun changeSongId(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("songId", nowPos)
        editor.apply()
    }
    private fun changeSong(){

        if (songs[nowPos].isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        MusicPlayServiceUtil.stopService(this@SongActivity)
        miniPlayerViewModel.resetViewModel()
        miniPlayerViewModel.updateMiniPlayerUI(songs[nowPos].title, songs[nowPos].singer)
        binding.songAlbumIv.setImageResource(songs[nowPos].coverImg!!)
        miniPlayerViewModel.musicPlay.value = true
        miniPlayerViewModel.isMusicTimeOver.value = false
        miniPlayerViewModel.musicPlayOrPause()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        CoroutineScope(Dispatchers.IO).launch {
            songs.addAll(songDB.SongDao().getSongs())
            withContext(Dispatchers.Main){
                miniPlayerViewModel.updateMiniPlayerUI(songs[nowPos].title, songs[nowPos].singer)

                val musicTime = intent.getIntExtra("musicTime", 0)
                val musicPlay = intent.getBooleanExtra("musicPlay", false)

                miniPlayerViewModel.setMusicTime(musicTime)
                updateTimerText(musicTime)
                miniPlayerViewModel.musicPlay.value = musicPlay
                binding.songAlbumIv.setImageResource(songs[nowPos].coverImg!!)

                if (songs[nowPos].isLike){
                    binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
                }else{
                    binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
                }
            }
        }
    }

    private fun goMainActivity(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("songId", nowPos)
        editor.apply()

        val intent = Intent(this@SongActivity, MainActivity::class.java)

//        intent.putExtra("musicTitle", miniPlayerViewModel.musicPlayTitle.value)
//        intent.putExtra("musicSinger", miniPlayerViewModel.musicPlayArtist.value)
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