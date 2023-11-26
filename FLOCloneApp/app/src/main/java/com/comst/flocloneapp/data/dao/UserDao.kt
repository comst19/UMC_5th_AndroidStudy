package com.comst.flocloneapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.comst.flocloneapp.model.local.UserEntity

@Dao
interface UserDao {

    @Insert
    fun insert(user: UserEntity)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): UserEntity?
}