package com.sevenexp.craftit.data.source.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity


@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: List<HistoryEntity>)

    @Update
    suspend fun updateStep(history: HistoryEntity)

    @Delete
    suspend fun deleteHistory(history: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history")
    suspend fun getAllHistory(): List<HistoryEntity>
}