package com.comst.flocloneapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ActivityMainBinding
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == 100){
            val musicTitle = result.data?.getStringExtra("musicTitle")
            val musicSinger = result.data?.getStringExtra("musicSinger")

            val albumIncludeMusic = AlbumIncludeMusic(0, musicTitle!!, false, musicSinger!!)
            mainViewModel.updateMiniPlayerUI(albumIncludeMusic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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

        binding.vm = mainViewModel

        binding.playMusicLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("musicTitle", mainViewModel.musicPlayTitle.value)
            intent.putExtra("musicSinger", mainViewModel.musicPlayArtist.value)
            registerLauncher.launch(intent)
        }

        setObserve()
    }

    fun setObserve(){
        mainViewModel.musicPlayArtist.observe(this){
            binding.musicPlayArtist.text = it
        }
        mainViewModel.musicPlayTitle.observe(this){
            binding.musicPlayTitle.text = it
        }
    }
}