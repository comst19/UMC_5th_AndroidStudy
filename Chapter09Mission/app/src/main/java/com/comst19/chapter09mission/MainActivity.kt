package com.comst19.chapter09mission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.comst19.chapter09mission.Coins.*
import com.comst19.chapter09mission.ExchangeRate.ExchangeConnection
import com.comst19.chapter09mission.ExchangeRate.ExchangeRateInfo
import com.comst19.chapter09mission.ExchangeRate.ExchangeRateService
import com.comst19.chapter09mission.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : CoinAdapter
    private var coinList = mutableListOf<CoinlInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUSDInformation()
        getCoinInformation()

        binding.temperatureSeeBtn.setOnClickListener {
            val intent = Intent(this, TemperatureActivity::class.java)
            startActivity(intent)
        }

        binding.refresh.setOnClickListener {
            getUSDInformation()
        }
    }

    private fun getUSDInformation() {
        val exchangeRateCall = ExchangeConnection.getInstance().create(ExchangeRateService::class.java)
        exchangeRateCall.getUSDInfo().enqueue(object  : Callback<List<ExchangeRateInfo>>{

            override fun onResponse(
                call: Call<List<ExchangeRateInfo>>,
                response: Response<List<ExchangeRateInfo>>
            ) {
                val exchangeRateList = response.body()
                if (exchangeRateList != null) {
                    val exchangeRateInfo = exchangeRateList[0]
                    runOnUiThread {
                        binding.dateTV.text = exchangeRateInfo.date
                        binding.usdTV.text = "KRW : USD = 1000 : ${exchangeRateInfo.price}"
                    }
                }
            }

            override fun onFailure(call: Call<List<ExchangeRateInfo>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getCoinInformation(){
        val coinListCall = CoinConnection.getInstance().create(CoinService::class.java)
        coinListCall.getCurrentCoinList().enqueue(object  : Callback<CoinInfoList>{
            override fun onResponse(call: Call<CoinInfoList>, response: Response<CoinInfoList>) {
                if (response.isSuccessful){

                    for (coin in response.body()!!.data){

                        try {
                            val gson = Gson()
                            val gsonToJson = gson.toJson(response.body()!!.data.get(coin.key))
                            val gsonFromJson = gson.fromJson(gsonToJson, CoinDetailInfo::class.java)

                            val currentCoin = CoinlInfo(coin.key, gsonFromJson)
                            coinList.add(currentCoin)
                        }catch (e : Exception){
                            Log.d("minsu","성공")
                        }

                    }

                    adapter = CoinAdapter(coinList)
                    binding.RV.adapter = adapter
                    binding.RV.layoutManager = LinearLayoutManager(applicationContext)
                }
            }

            override fun onFailure(call: Call<CoinInfoList>, t: Throwable) {
                Toast.makeText(this@MainActivity, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_LONG).show()
            }

        })
    }

}