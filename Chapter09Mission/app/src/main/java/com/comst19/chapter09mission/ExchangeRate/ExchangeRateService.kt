package com.comst19.chapter09mission.ExchangeRate

import retrofit2.Call
import retrofit2.http.GET


interface ExchangeRateService {

    @GET("exchange/rate/KRW/USD.json")
    fun getUSDInfo() : Call<List<ExchangeRateInfo>>
}