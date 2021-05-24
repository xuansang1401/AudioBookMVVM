package com.t3h.basemvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.t3h.basemvvm.data.room.History

@Database(version = 1, exportSchema = false,
    entities = [History::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
    companion object {
        private val NAME_DB="AppName"
        @Volatile
        private var instance: AppDatabase? = null

        fun create(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java,
                NAME_DB
            ).allowMainThreadQueries().build()
        }

    }

}