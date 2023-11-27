package com.comst19.chapter09mission.ExchangeRate

import com.google.gson.annotations.SerializedName

data class ExchangeRateInfo (
    @SerializedName("date")
    val date : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("rate")
    val price : Double
)