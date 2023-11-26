package com.comst.flocloneapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserTable")
data class UserEntity(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}