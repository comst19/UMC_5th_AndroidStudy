package com.comst.flocloneapp.data.network

import com.comst.flocloneapp.model.local.UserEntity
import com.comst.flocloneapp.model.network.AuthResponse
import com.comst.flocloneapp.ui.response.LoginView
import com.comst.flocloneapp.ui.response.SignUpView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class FLOAuthService {


    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
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
}