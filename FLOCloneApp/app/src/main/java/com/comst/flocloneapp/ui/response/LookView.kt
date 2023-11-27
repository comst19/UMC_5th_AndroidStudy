package com.comst.flocloneapp.ui.response

import com.comst.flocloneapp.model.network.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}