package com.comst19.chapter09mission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.comst19.chapter09mission.databinding.ActivityTemperatureBinding
import com.comst19.chapter09mission.temperature.RiverInfo
import com.comst19.chapter09mission.temperature.TemperatureConnection
import com.comst19.chapter09mission.temperature.TemperatureService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// https://hangang.life/api/

class TemperatureActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTemperatureBinding
    private var temperature : Double = 0.00
    private var date : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTemperatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLiverInfo()


    }

    private fun getLiverInfo(){

        val riverCall = TemperatureConnection.getInstance().create(TemperatureService::class.java)
        riverCall.getLiverInfo().enqueue(object : Callback<RiverInfo> {
            override fun onResponse(call: Call<RiverInfo>, response: Response<RiverInfo>) {
                if (response.isSuccessful) {
                    val liverInfo = response.body()
                    if (liverInfo != null) {
                        runOnUiThread {
                            binding.tempTV.text = "${liverInfo.temperature} °C"
                            binding.dateTV.text = liverInfo.date
                        }
                    }
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<RiverInfo>, t: Throwable) {
                t.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@TemperatureActivity, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}