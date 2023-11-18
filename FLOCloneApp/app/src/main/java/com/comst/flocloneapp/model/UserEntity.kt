package com.comst.flocloneapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class UserEntity(
    val email: String,
    val emailAddress : String,
    val password: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}