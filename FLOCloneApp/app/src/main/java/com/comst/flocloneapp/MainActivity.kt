package com.comst.flocloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.comst.flocloneapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == 100){
            val musicTitle = result.data?.getStringExtra("musicTitle")
            val musicSinger = result.data?.getStringExtra("musicSinger")

            binding.musicPlayTitle.text = musicTitle
            binding.musicPlayArtist.text = musicSinger
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host)
        if (navController.currentDestination?.id == R.id.homeFragment) {
            // 현재 화면이 홈 프래그먼트일 경우 앱을 백그라운드로 보냅니다.
            super.onBackPressed()
        } else {
            // 그렇지 않으면 홈 프래그먼트로 이동합니다.
            navController.navigate(R.id.homeFragment)
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
                        navController.navigate(R.id.homeFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.lookFragment -> {
                        navController.navigate(R.id.lookFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.searchFragment -> {
                        navController.navigate(R.id.searchFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.nav_bottom_graph, true)
                            .build())
                        true
                    }
                    R.id.lockerFragment -> {
                        navController.navigate(R.id.lockerFragment, null, NavOptions.Builder()
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
            registerLauncher.launch(intent)
        }
    }
}