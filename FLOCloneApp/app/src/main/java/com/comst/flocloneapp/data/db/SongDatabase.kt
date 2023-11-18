package com.comst.flocloneapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.comst.flocloneapp.data.dao.AlbumDao
import com.comst.flocloneapp.data.dao.SongDao
import com.comst.flocloneapp.data.dao.UserDao
import com.comst.flocloneapp.model.AlbumEntity
import com.comst.flocloneapp.model.LikeEntity
import com.comst.flocloneapp.model.SongEntity
import com.comst.flocloneapp.model.UserEntity

@Database(entities = [SongEntity::class, AlbumEntity::class, UserEntity::class, LikeEntity::class],  version = 1)
abstract class SongDatabase : RoomDatabase() {

    abstract fun SongDao() : SongDao
    abstract fun AlbumDao(): AlbumDao

    abstract fun UserDao(): UserDao

    companion object {
        private var instance : SongDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : SongDatabase? {
            if (instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return instance
        }
    }
}