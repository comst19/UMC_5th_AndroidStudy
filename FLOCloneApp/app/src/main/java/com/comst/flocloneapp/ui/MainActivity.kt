package com.comst.flocloneapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.ActivityMainBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.model.SongEntity
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val miniPlayerViewModel : MiniPlayerViewModel by viewModels()

    private lateinit var song : SongEntity
    val songs = arrayListOf<SongEntity>()
    lateinit var songDB : SongDatabase
    var nowPos = 0

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == 100){
            val spf = getSharedPreferences("song", MODE_PRIVATE)
            nowPos = spf.getInt("songId",0)

            miniPlayerViewModel.updateMiniPlayerUI(songs[nowPos].title, songs[nowPos].singer)

            val musicTime = result.data?.getIntExtra("musicTime", 0)
            val musicPlay = result.data?.getBooleanExtra("musicPlay", false)

            miniPlayerViewModel.updateMiniPlayerUI(songs[nowPos].title, songs[nowPos].singer)

            miniPlayerViewModel.setMusicTime(musicTime!!)
            miniPlayerViewModel.musicPlay.value = musicPlay

            val musicTitle = result.data?.getStringExtra("musicTitle")
            val musicSinger = result.data?.getStringExtra("musicSinger")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host)

        inputDummySongs()


        NavigationUI.setupWithNavController(binding.navBottomBar, findNavController(R.id.nav_host))

        binding.navBottomBar.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                when (item.itemId) {
                    R.id.homeFragment -> {
                        navController.navigate(
                            R.id.homeFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.lookFragment -> {
                        navController.navigate(
                            R.id.lookFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.searchFragment -> {
                        navController.navigate(
                            R.id.searchFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.lockerFragment -> {
                        navController.navigate(
                            R.id.lockerFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }

        binding.playMusicLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("musicTitle", miniPlayerViewModel.musicPlayTitle.value)
            intent.putExtra("musicSinger", miniPlayerViewModel.musicPlayArtist.value)
            intent.putExtra("musicTime", miniPlayerViewModel.musicTime.value)
            intent.putExtra("musicPlay", miniPlayerViewModel.musicPlay.value)
            intent.putExtra("songId", nowPos)
            registerLauncher.launch(intent)

        }

        initView()
        setObserve()
    }

    fun setObserve(){
        miniPlayerViewModel.musicPlayArtist.observe(this){
            binding.musicPlayArtist.text = it
        }
        miniPlayerViewModel.musicPlayTitle.observe(this){
            binding.musicPlayTitle.text = it
        }

        miniPlayerViewModel.isMusicTimeOver.observe(this) { isOver ->
            if(isOver) {
                MusicPlayServiceUtil.stopService(this)
            }
        }

        miniPlayerViewModel.musicTime.observe(this){
            miniPlayerViewModel.checkMusicTimeAndStop()
        }

        miniPlayerViewModel.musicPlay.observe(this) {
            if (it) {
                MusicPlayServiceUtil.startService(this@MainActivity)
            } else {
                MusicPlayServiceUtil.pauseService(this@MainActivity)
            }
        }

    }

    fun initView(){

        with(binding){
            vm = miniPlayerViewModel
            lifecycleOwner = this@MainActivity

            playMusicStart.setOnClickListener {
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
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    miniPlayerViewModel.setMusicTime(songProgressSb.progress)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    miniPlayerViewModel.setMusicTime(songProgressSb.progress)
                }

            })

            songNextIv.setOnClickListener {
                miniPlayerViewModel.setMusicTime((songProgressSb.progress + 5).coerceAtMost(songProgressSb.max))

            }
            songPreviousIv.setOnClickListener {
                miniPlayerViewModel.setMusicTime((songProgressSb.progress - 5).coerceAtLeast(0))
            }

        }
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        CoroutineScope(Dispatchers.IO).launch {
            songs.addAll(songDB.SongDao().getSongs())
            withContext(Dispatchers.Main){
                val spf = getSharedPreferences("song", MODE_PRIVATE)
                nowPos = spf.getInt("songId",0)
                miniPlayerViewModel.updateMiniPlayerUI(songs[nowPos].title, songs[nowPos].singer)
            }
        }
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!

        CoroutineScope(Dispatchers.IO).launch {
            val songs = songDB.SongDao().getSongs()

            if (songs.isNotEmpty()) return@launch
            else{
                songDB.SongDao().insert(
                    SongEntity(
                        "Lilac",
                        "아이유 (IU)",
                        0,
                        200,
                        false,
                        "music_lilac",
                        R.drawable.img_album_exp2,
                        false,
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Flu",
                        "아이유 (IU)",
                        0,
                        200,
                        false,
                        "music_flu",
                        R.drawable.img_album_exp2,
                        false,
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Butter",
                        "방탄소년단 (BTS)",
                        0,
                        190,
                        false,
                        "music_butter",
                        R.drawable.img_album_exp,
                        false,
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Next Level",
                        "에스파 (AESPA)",
                        0,
                        210,
                        false,
                        "music_next",
                        R.drawable.img_album_exp3,
                        false,
                    )
                )


                songDB.SongDao().insert(
                    SongEntity(
                        "Boy with Luv",
                        "music_boy",
                        0,
                        230,
                        false,
                        "music_lilac",
                        R.drawable.img_album_exp4,
                        false,
                    )
                )


                songDB.SongDao().insert(
                    SongEntity(
                        "BBoom BBoom",
                        "모모랜드 (MOMOLAND)",
                        0,
                        240,
                        false,
                        "music_bboom",
                        R.drawable.img_album_exp5,
                        false,
                    )
                )
            }
        }

        initPlayList()
    }
}