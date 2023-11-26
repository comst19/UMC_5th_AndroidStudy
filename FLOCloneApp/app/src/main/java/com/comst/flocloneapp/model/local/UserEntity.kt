package com.comst.flocloneapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserTable")
data class UserEntity(
    val email: String,
    val password: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}