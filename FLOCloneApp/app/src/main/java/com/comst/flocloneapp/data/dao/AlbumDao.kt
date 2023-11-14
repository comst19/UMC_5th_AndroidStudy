package com.comst.flocloneapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.comst.flocloneapp.model.AlbumEntity

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: AlbumEntity)

    @Update
    fun update(album: AlbumEntity)

    @Delete
    fun delete(album: AlbumEntity)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): AlbumEntity

    @Insert
    fun likeAlbum(like: AlbumEntity)

}