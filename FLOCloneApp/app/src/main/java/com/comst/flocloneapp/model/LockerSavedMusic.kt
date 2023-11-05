package com.comst.flocloneapp.model

data class LockerSavedMusic (
    val id : Int,
    val musicImg : Int,
    val musicName : String,
    val artist : String,
    var switchOnOff : Boolean
)