package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.LockerSavedMusic

interface SavedMusicListener {

    fun savedSongsPlay(savedMusic: LockerSavedMusic)
    fun deleteSavedSong(position : Int)
}