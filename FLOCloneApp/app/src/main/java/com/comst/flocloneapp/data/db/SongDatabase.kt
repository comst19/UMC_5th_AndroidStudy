package com.comst.flocloneapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.comst.flocloneapp.data.dao.SongDao
import com.comst.flocloneapp.model.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract class SongDatabase : RoomDatabase() {

    abstract fun SongDao() : SongDao

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
                    ).build()
                }
            }

            return instance
        }
    }
}