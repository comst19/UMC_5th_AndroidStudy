package com.comst.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comst.memo.databinding.ActivityCheckBinding

class CheckActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.memoTV.text = intent.getStringExtra("memo")

    }
}