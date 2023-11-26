package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.local.SongEntity

interface SavedMusicListener {

    fun savedSongsPlay(savedMusic: SongEntity)
    fun deleteSavedSong(position : Int)
}