package com.comst.flocloneapp.util

import android.content.Context
import android.content.Intent
import com.comst.flocloneapp.service.MusicPlayService

object MusicPlayServiceUtil {

    fun startService(context: Context) {
        val intent = Intent(context, MusicPlayService::class.java)
        intent.action = "PLAY"
        context.startService(intent)
    }

    fun pauseService(context: Context) {
        val intent = Intent(context, MusicPlayService::class.java)
        intent.action = "PAUSE"
        context.startService(intent)
    }

    fun stopService(context: Context) {
        val intent = Intent(context, MusicPlayService::class.java)
        intent.action = "STOP"
        context.startService(intent)
    }
}