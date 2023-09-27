package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.AlbumIncludeMusic

interface PlayMusicListener {
    fun albumIncludedSongsPlay(albumIncludeMusic: AlbumIncludeMusic)
}