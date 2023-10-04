package com.comst.flocloneapp.ui

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ActivityMainBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.viewmodel.MainViewModel
import com.comst.flocloneapp.viewmodel.MiniPlayerFactory
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var miniPlayerViewModel: MiniPlayerViewModel


    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == 100){
            val musicTitle = result.data?.getStringExtra("musicTitle")
            val musicSinger = result.data?.getStringExtra("musicSinger")

            val albumIncludeMusic = AlbumIncludeMusic(0, musicTitle!!, false, musicSinger!!)
            //mainViewModel.updateMiniPlayerUI(albumIncludeMusic)
            miniPlayerViewModel.updateMiniPlayerUI(albumIncludeMusic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host)
        NavigationUI.setupWithNavController(binding.navBottomBar, findNavController(R.id.nav_host))

        miniPlayerViewModel = ViewModelProvider(
            this@MainActivity,
            MiniPlayerFactory(application)
        ).get(MiniPlayerViewModel::class.java)

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

        binding.vm = miniPlayerViewModel

        binding.playMusicLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("musicTitle", miniPlayerViewModel.musicPlayTitle.value)
            intent.putExtra("musicSinger", miniPlayerViewModel.musicPlayArtist.value)
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

        miniPlayerViewModel.musicTime.observe(this){
            if (it == 60){
                miniPlayerViewModel.job?.cancel()
                miniPlayerViewModel.musicPlay.value = false
            }
        }


        miniPlayerViewModel.musicPlay.observe(this){
            if (it){
                binding.playMusicStart.setImageDrawable(
                    AppCompatResources.getDrawable(this@MainActivity,
                    R.drawable.btn_miniplay_pause
                ))
                miniPlayerViewModel.job = CoroutineScope(Dispatchers.Main).launch {
                    while (it && binding.songProgressSb.progress < binding.songProgressSb.max){
                        delay(1000)
                        miniPlayerViewModel.setMusicTime(miniPlayerViewModel.musicTime.value?.plus(1) ?: 0)
                    }
                }
            }else{
                binding.playMusicStart.setImageDrawable(
                    AppCompatResources.getDrawable(this@MainActivity,
                    R.drawable.btn_miniplayer_play
                ))
                miniPlayerViewModel.job?.cancel()
            }
        }

    }

    fun initView(){

        with(binding){
            vm = miniPlayerViewModel
            lifecycleOwner = this@MainActivity

            playMusicStart.setOnClickListener {
                miniPlayerViewModel.musicPlay.value = !miniPlayerViewModel.musicPlay.value!!
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