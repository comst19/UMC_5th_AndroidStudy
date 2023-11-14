package com.comst.flocloneapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.comst.flocloneapp.model.SongEntity

@Dao
interface SongDao {

    @Insert
    fun insert(song : SongEntity)

    @Update
    fun update(song : SongEntity)

    @Delete
    fun delete(song : SongEntity)

    @Query("SELECT * FROM SongTable")
    fun getSongs() : List<SongEntity>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id : Int) : SongEntity

    @Query("SELECT * FROM SongTable WHERE isLike = 1")
    fun getLikedSongs() : List<SongEntity>

    @Query("UPDATE SongTable SET isLike = 0")
    fun setAllLikesToFalse()

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getIncludeSong(albumIdx : Int) : List<SongEntity>
}