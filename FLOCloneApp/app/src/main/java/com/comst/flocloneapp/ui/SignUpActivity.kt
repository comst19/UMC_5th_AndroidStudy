package com.comst.flocloneapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.ActivitySignUpBinding
import com.comst.flocloneapp.model.UserEntity
import com.comst.flocloneapp.util.ToastLikeOnOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

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

                CoroutineScope(Dispatchers.IO).launch {

                    val email = signUpIdEt.text.toString()
                    val address = signUpDirectInputEt.text.toString()
                    val name = signUpNameEt.text.toString()
                    val password = signUpPasswordEt.text.toString()

                    val user = UserEntity(email, address, password, name)

                    songDB.UserDao().insert(user)
                }

                ToastLikeOnOff.createToast(this@SignUpActivity, "회원가입 되었습니다.")?.show()
                finish()

            }
        }
    }

    private fun check() : Boolean{
        with(binding){
            return !(signUpIdEt.text.toString().isBlank() || signUpDirectInputEt.text.toString().isBlank() ||
                    signUpNameEt.text.toString().isBlank() || signUpPasswordEt.text.toString().isBlank() || signUpPasswordCheckEt.text.toString().isBlank())
        }
    }
}