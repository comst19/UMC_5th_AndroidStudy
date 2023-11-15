package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.model.SongEntity

interface PlayMusicListener {
    fun albumIncludedSongsPlay(albumIncludeMusic: SongEntity)
}