package com.comst.flocloneapp.ui.response

import com.comst.flocloneapp.model.network.Result

interface AutoLoginView {
    fun onLoginSuccess()
    fun onLoginFailure()
}