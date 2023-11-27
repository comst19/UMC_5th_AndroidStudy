package com.comst19.chapter09mission.temperature

import com.google.gson.annotations.SerializedName

data class RiverInfo (
    @SerializedName("temp")
    val temperature : String,

    @SerializedName("time")
    val date : String

)