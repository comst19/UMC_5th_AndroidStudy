package com.comst.flocloneapp.listener

import com.comst.flocloneapp.model.local.SongEntity

interface PlayMusicListener {
    fun albumIncludedSongsPlay(albumIncludeMusic: SongEntity)
}