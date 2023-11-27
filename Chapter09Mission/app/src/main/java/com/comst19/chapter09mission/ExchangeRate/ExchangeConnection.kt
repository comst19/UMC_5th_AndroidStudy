package com.comst19.chapter09mission.ExchangeRate

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD

class ExchangeConnection {

    companion object {
        private const val BASE_URL1 = "https://api.manana.kr/"
        //private const val BASE_URL1 = "https://quotation-api-cdn.dunamu.com/"
        private var INSTANCE1 : Retrofit? = null

        fun getInstance() : Retrofit {
            if(INSTANCE1 == null) {
                INSTANCE1 = Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return INSTANCE1!!
        }
    }
}