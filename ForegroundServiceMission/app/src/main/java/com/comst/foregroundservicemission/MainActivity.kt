package com.comst.foregroundservicemission

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.comst.foregroundservicemission.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { ok ->
            if (ok) {
                // 알림권한 허용 o
            } else {
                // 알림권한 허용 x
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

        binding.start.setOnClickListener {
            serviceStart()
            Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show()
        }

        binding.stop.setOnClickListener {
            serviceStop()
            Toast.makeText(this, "중지", Toast.LENGTH_SHORT).show()
        }

    }

    fun serviceStart(){
        val intent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    fun serviceStop(){
        val intent = Intent(this, ForegroundService::class.java)
        stopService(intent)
    }

    fun requestNotificationPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 다른 런타임 퍼미션이랑 비슷한 과정
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // 왜 알림을 허용해야하는지 유저에게 알려주기를 권장
                } else {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                // 안드로이드 12 이하는 알림에 런타임 퍼미션 없으니, 설정가서 켜보라고 권해볼 수 있겠다.
            }
        }
    }
}