package com.comst.flocloneapp.data

import com.comst.flocloneapp.data.network.FLOAuthInterface
import com.comst.flocloneapp.data.network.FLOSongInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FLORetrofitManager {

    val BASE_URL = "https://edu-api-test.softsquared.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getAuthService(): FLOAuthInterface = retrofit.create(FLOAuthInterface::class.java)

    fun getSongService(): FLOSongInterface = retrofit.create(FLOSongInterface::class.java)

}