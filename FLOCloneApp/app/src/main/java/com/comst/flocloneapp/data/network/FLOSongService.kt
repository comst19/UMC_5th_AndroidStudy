package com.comst.flocloneapp.data.network

import com.comst.flocloneapp.data.FLORetrofitManager
import com.comst.flocloneapp.model.network.SongResponse
import com.comst.flocloneapp.ui.response.LookView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FLOSongService {

    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView) {
        this.lookView = lookView
    }

    fun getSongs() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FLORetrofitManager.getSongService().getSongs()

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful && response.code() == 200){
                        val songResponse : SongResponse = response.body()!!
                        when(val code = songResponse.code){
                            1000 -> lookView.onGetSongSuccess(code, songResponse.result)
                            else -> lookView.onGetSongFailure(code, songResponse.message)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    lookView.onGetSongFailure(400, "네트워크 오류가 발생했습니다.")
                }
            }
        }
    }
}