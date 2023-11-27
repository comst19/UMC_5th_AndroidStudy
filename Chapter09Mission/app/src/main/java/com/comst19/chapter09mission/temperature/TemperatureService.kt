package com.comst19.chapter09mission.temperature

import retrofit2.Call
import retrofit2.http.GET

interface TemperatureService {

    @GET("/")
    fun getLiverInfo() : Call<RiverInfo>
}