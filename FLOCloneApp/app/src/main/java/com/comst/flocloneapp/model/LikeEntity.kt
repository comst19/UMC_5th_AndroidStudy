package com.comst.flocloneapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class LikeEntity(var userId: Int, var albumId: Int) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}