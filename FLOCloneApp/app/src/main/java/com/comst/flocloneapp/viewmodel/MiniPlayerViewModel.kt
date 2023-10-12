package com.comst.flocloneapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comst.flocloneapp.model.AlbumIncludeMusic
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MiniPlayerViewModel : ViewModel() {

    var job : Job? = null


    private val _musicPlayTitle = MutableLiveData<String>("재생목록이 비었습니다.")
    val musicPlayTitle : LiveData<String> get() = _musicPlayTitle

    private val _musicPlayArtist = MutableLiveData<String>("")

    val musicPlayArtist : LiveData<String> get() = _musicPlayArtist
    val musicPlay = MutableLiveData<Boolean>(false)

    private val _musicTime = MutableLiveData<Int>(0)
    val musicTime : LiveData<Int> get() = _musicTime

    fun updateMiniPlayerUI(albumIncludeMusic : AlbumIncludeMusic){
        _musicPlayTitle.value = albumIncludeMusic.musicName
        _musicPlayArtist.value = albumIncludeMusic.artist
    }

    fun resetViewModel() {
        _musicPlayTitle.value = "재생목록이 비었습니다."
        _musicPlayArtist.value = ""
        musicPlay.value = false
        _musicTime.value = 0
        job?.cancel()
    }

    fun setMusicTime(progress : Int){
        _musicTime.value = progress
    }

    fun musicPlayOrPause() {
        if (musicPlay.value == true) {
            if (job?.isActive == true) return
            job = viewModelScope.launch {
                while (musicPlay.value == true && (_musicTime.value ?: 0) < MAX_MUSIC_TIME) {
                    delay(1000)
                    _musicTime.value = (_musicTime.value ?: 0) + 1
                }
            }
        } else {
            job?.cancel()
        }
    }

    companion object {
        private const val MAX_MUSIC_TIME = 60
    }
}