package com.comst.flocloneapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ActivityMainBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val miniPlayerViewModel : MiniPlayerViewModel by viewModels()

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == 100){

            val musicTitle = result.data?.getStringExtra("musicTitle")
            val musicSinger = result.data?.getStringExtra("musicSinger")
            val musicTime = result.data?.getIntExtra("musicTime", 0)
            val musicPlay = result.data?.getBooleanExtra("musicPlay", false)

            val albumIncludeMusic = AlbumIncludeMusic(0, musicTitle!!, false, musicSinger!!)
            miniPlayerViewModel.updateMiniPlayerUI(albumIncludeMusic.musicName, albumIncludeMusic.artist)

            miniPlayerViewModel.setMusicTime(musicTime!!)
            miniPlayerViewModel.musicPlay.value = musicPlay

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host)
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
}