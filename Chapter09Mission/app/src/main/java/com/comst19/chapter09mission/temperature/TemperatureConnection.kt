package com.comst19.chapter09mission.temperature

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TemperatureConnection {

    companion object{
        private const val BASE_URL = "http://hangang.dkserver.wo.tc/"
        private var INSTANCE : Retrofit? = null

        fun getInstance() : Retrofit{
            if(INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return INSTANCE!!
        }
    }

}