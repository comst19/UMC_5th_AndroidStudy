package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.TodayMusic

interface ItemTodayMusicListener {

    fun goAlbumFragment(position : Int)
    fun playMusic(todayMusic : TodayMusic)
}