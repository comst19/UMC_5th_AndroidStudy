package com.comst.memo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.comst.memo.databinding.ActivityMainBinding

var data = ""

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){}

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            intent.putExtra("memo",binding.edit.text.toString())
            requestLauncher.launch(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("lifecycle","onPause")
        data = binding.edit.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle","onRestart")
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Memo")
        dialog.setMessage("메모를 다시 작성하시겠습니까?")

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {}
                DialogInterface.BUTTON_NEGATIVE -> {
                    data = ""
                    binding.edit.setText(data)
                }
            }
        }
        dialog.setPositiveButton("YES", listener)
        dialog.setNegativeButton("NO", listener)
        dialog.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle","onResume")
        binding.edit.setText(data)
    }
}