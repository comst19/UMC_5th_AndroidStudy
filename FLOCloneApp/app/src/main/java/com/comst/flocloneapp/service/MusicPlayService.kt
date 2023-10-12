package com.comst.flocloneapp.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.comst.flocloneapp.R

class MusicPlayService : Service() {

    lateinit var mp : MediaPlayer

    override fun onBind(intent: Intent): IBinder {
        // Service 객체와 통신할 떄 사용
        TODO("Return the communication channel to the service")
    }

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.music_lilac)
        mp.isLooping = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when(action) {
            "PLAY" -> startMusic()
            "PAUSE" -> pauseMusic()
            "RESUME" -> resumeMusic()
            "STOP" -> stopMusic()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mp.release()
    }

    private fun startMusic() {
        if (!mp.isPlaying) {
            mp.start()
        }
    }

    private fun pauseMusic() {
        if (mp.isPlaying) {
            mp.pause()
        }
    }

    private fun resumeMusic() {
        if (!mp.isPlaying) {
            mp.start()
        }
    }

    private fun stopMusic() {
        if (mp.isPlaying) {
            mp.stop()
            mp.prepare()
        }
    }
}