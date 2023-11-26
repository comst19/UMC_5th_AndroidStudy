package com.comst.flocloneapp.ui.response

import com.comst.flocloneapp.model.network.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}