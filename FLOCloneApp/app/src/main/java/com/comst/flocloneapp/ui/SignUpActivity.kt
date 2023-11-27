package com.comst.flocloneapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.data.network.FLOAuthService
import com.comst.flocloneapp.databinding.ActivitySignUpBinding
import com.comst.flocloneapp.model.local.UserEntity
import com.comst.flocloneapp.ui.response.SignUpView
import com.comst.flocloneapp.util.ToastLikeOnOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity(), SignUpView {

    private lateinit var binding : ActivitySignUpBinding

    lateinit var songDB : SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songDB = SongDatabase.getInstance(this)!!

        initView()
    }

    private fun initView(){

        with(binding){

            signUpSignUpBtn.setOnClickListener {

                if (!check()) {
                    ToastLikeOnOff.createToast(this@SignUpActivity, "빈칸을 채워주세요")?.show()
                    return@setOnClickListener

                }
                networkSignUp()
                //localSignUp()
            }
        }
    }

    private fun localSignUp(){
        CoroutineScope(Dispatchers.IO).launch {
            songDB.UserDao().insert(inputUserData())
        }

        ToastLikeOnOff.createToast(this@SignUpActivity, "회원가입 되었습니다.")?.show()
        finish()
    }

    private fun networkSignUp(){
        val authService = FLOAuthService()
        authService.setSignUpView(this)
        authService.signUp(inputUserData())
    }

    private fun inputUserData(): UserEntity {
        val email = "${binding.signUpIdEt.text}@${binding.signUpDirectInputEt.text}"
        val name = binding.signUpNameEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()

        return UserEntity(email, password, name)
    }

    private fun check() : Boolean{
        with(binding){
            return !(signUpIdEt.text.toString().isBlank() || signUpDirectInputEt.text.toString().isBlank() ||
                    signUpNameEt.text.toString().isBlank() || signUpPasswordEt.text.toString().isBlank() || signUpPasswordCheckEt.text.toString().isBlank())
        }
    }

    override fun onSignUpSuccess() {
        ToastLikeOnOff.createToast(this@SignUpActivity, "회원가입 되었습니다.")?.show()
        finish()
    }

    override fun onSignUpFailure(message : String) {
        ToastLikeOnOff.createToast(this@SignUpActivity, message)?.show()
    }
}