package com.comst.flocloneapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class SongEntity(

    var title : String = "",
    var singer : String = "",
    var second : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = false,
    var music : String,
    var coverImg : Int? = null,
    var isLike : Boolean = false
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
