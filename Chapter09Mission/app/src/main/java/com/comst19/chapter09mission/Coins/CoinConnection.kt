package com.comst19.chapter09mission.Coins

import com.comst19.chapter09mission.temperature.TemperatureConnection
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinConnection {

    companion object{
        private const val BASE_URL2 = "https://api.bithumb.com/"
        private var INSTANCE2 : Retrofit? = null

        fun getInstance() : Retrofit{
            if(INSTANCE2 == null) {
                INSTANCE2 = Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return INSTANCE2!!
        }
    }

}