package com.comst.flocloneapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.data.network.FLOAuthService
import com.comst.flocloneapp.databinding.ActivityLoginBinding
import com.comst.flocloneapp.model.local.UserEntity
import com.comst.flocloneapp.model.network.Result
import com.comst.flocloneapp.ui.response.LoginView
import com.comst.flocloneapp.util.ToastLikeOnOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var binding : ActivityLoginBinding

    lateinit var songDB : SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!


        initView()
    }

    private fun initView(){

        with(binding){

            root.setPadding(0,getStatusBarHeight(this@LoginActivity), 0, 0)

            loginSignUpTv.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            loginCloseIv.setOnClickListener {
                finish()
            }

            loginSignInBtn.setOnClickListener {

                if (!check()){
                    ToastLikeOnOff.createToast(this@LoginActivity, "이메일, 비밀번호를 올바르게 입력하세요")?.show()
                    return@setOnClickListener
                }
                networkLogin()
                //localSignIn()
            }
        }
    }

    private fun localSignIn(){
        CoroutineScope(Dispatchers.IO).launch {
            val email = "${binding.loginIdEt.text}@${binding.loginDirectInputEt.text}"
            val password = binding.loginPasswordEt.text.toString()
            val currentUser = songDB.UserDao().getUser(email, password)

            if (currentUser == null){
                withContext(Dispatchers.Main){
                    ToastLikeOnOff.createToast(this@LoginActivity, "존재하지 않는 유저입니다.")?.show()
                }
            }else{
                val spf = getSharedPreferences("song", MODE_PRIVATE)
                val editor = spf.edit()
                editor.putInt("currentUserIdx", currentUser.id)
                editor.apply()

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun networkLogin(){
        val authService = FLOAuthService()
        authService.setLoginView(this)
        authService.login(inputUserData())
    }



    private fun inputUserData(): UserEntity {
        val email = "${binding.loginIdEt.text}@${binding.loginDirectInputEt.text}"
        val password = binding.loginPasswordEt.text.toString()
        return UserEntity(email, password, "")
    }


    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun check() : Boolean{
        with(binding){
            return !(loginIdEt.text.toString().isBlank() || loginDirectInputEt.text.toString().isBlank() || loginPasswordEt.text.toString().isBlank())
        }
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("currentUserIdx", result.userIdx)
        editor.apply()
        editor.putString("currentUserJwt", result.jwt)
        editor.apply()

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()

    }

    override fun onLoginFailure(message : String) {
        ToastLikeOnOff.createToast(this@LoginActivity, message)?.show()
    }
}