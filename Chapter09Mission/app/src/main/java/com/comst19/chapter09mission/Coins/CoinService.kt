package com.comst19.chapter09mission.Coins

import retrofit2.Call
import retrofit2.http.GET

interface CoinService {

    @GET("public/ticker/ALL_KRW")
    fun getCurrentCoinList() : Call<CoinInfoList>

}