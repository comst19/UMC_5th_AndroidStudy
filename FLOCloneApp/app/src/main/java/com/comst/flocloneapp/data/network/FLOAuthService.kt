package com.comst.flocloneapp.data.network

import android.util.Log
import com.comst.flocloneapp.data.FLORetrofitManager
import com.comst.flocloneapp.model.local.UserEntity
import com.comst.flocloneapp.model.network.AuthResponse
import com.comst.flocloneapp.ui.response.AutoLoginView
import com.comst.flocloneapp.ui.response.LoginView
import com.comst.flocloneapp.ui.response.SignUpView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FLOAuthService {


    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: AutoLoginView) {
        this.autoLoginView = autoLoginView
    }


    fun signUp(user: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FLORetrofitManager.getAuthService().signUp(user)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.code() == 200) {
                        val signUpResponse: AuthResponse = response.body()!!
                        when (val code = signUpResponse.code) {
                            1000 -> signUpView.onSignUpSuccess()
                            else -> signUpView.onSignUpFailure(signUpResponse.message)
                        }
                    }
                }
            } catch (e: Exception) {
                //실패처리
            }
        }
    }

    fun login(user: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FLORetrofitManager.getAuthService().login(user)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.code() == 200) {
                        val loginResponse: AuthResponse = response.body()!!
                        when (val code = loginResponse.code) {
                            1000 -> loginView.onLoginSuccess(code, loginResponse.result!!)
                            else -> loginView.onLoginFailure(loginResponse.message)
                        }
                    }
                }
            } catch (e: Exception) {
                //실패처리
            }
        }
    }

    fun autoLogin(jwt : String){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FLORetrofitManager.getAuthService().autoLogin(jwt)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.code() == 200){
                        val autoLoginResponse : AuthResponse = response.body()!!
                        when(val code = autoLoginResponse.code){
                            1000 -> autoLoginView.onAutoLoginSuccess()
                            else -> autoLoginView.onAutoLoginFailure()
                        }
                    }else{

                    }
                }
            }catch (e : Exception){
                //실패처리
            }
        }
    }
}