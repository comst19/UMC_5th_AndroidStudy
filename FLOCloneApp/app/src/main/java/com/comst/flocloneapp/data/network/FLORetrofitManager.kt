package com.comst.flocloneapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FLORetrofitManager {

    val BASE_URL = "https://edu-api-test.softsquared.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(FLOService::class.java)

    fun getInstance(): FLOService{
        return api
    }

}