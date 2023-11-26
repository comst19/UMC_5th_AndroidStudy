package com.comst.flocloneapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.comst.flocloneapp.model.local.AlbumEntity
import com.comst.flocloneapp.model.local.LikeEntity

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
    fun likeAlbum(like: LikeEntity)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int): List<AlbumEntity>
}