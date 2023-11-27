package com.comst.flocloneapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.network.FLOAuthService
import com.comst.flocloneapp.model.network.Result
import com.comst.flocloneapp.ui.response.AutoLoginView
import com.comst.flocloneapp.ui.response.LoginView

class SplashActivity : AppCompatActivity(), AutoLoginView {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        autoLogin()
    }

    private fun autoLogin(){
        val authService = FLOAuthService()
        authService.setAutoLoginView(this)
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val jwt = spf.getString("currentUserJwt","")!!
        Log.d("jwt", jwt)
        authService.autoLogin(jwt)
    }

    override fun onAutoLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onAutoLoginFailure() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}