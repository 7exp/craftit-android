package com.sevenexp.craftit.data.source.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.widget.Converter
@Database(
    entities = [HistoryEntity::class, FypItems::class, RemoteKeys::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class HandicraftDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun fypDao(): FypDao

    companion object {
        @Volatile
        private var INSTANCE: HandicraftDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HandicraftDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    HandicraftDatabase::class.java,
                    "handicraft_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}