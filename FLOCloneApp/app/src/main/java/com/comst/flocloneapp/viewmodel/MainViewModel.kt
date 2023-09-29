package com.comst.flocloneapp.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comst.flocloneapp.model.AlbumIncludeMusic

class MainViewModel : ViewModel() {


    private val _musicPlayTitle = MutableLiveData<String>("재생목록이 비었습니다.")
    val musicPlayTitle : LiveData<String> get() = _musicPlayTitle

    private val _musicPlayArtist = MutableLiveData<String>("")
    val musicPlayArtist : LiveData<String> get() = _musicPlayArtist

    fun updateMiniPlayerUI(albumIncludeMusic : AlbumIncludeMusic){
        _musicPlayTitle.value = albumIncludeMusic.musicName
        _musicPlayArtist.value = albumIncludeMusic.artist
    }
}