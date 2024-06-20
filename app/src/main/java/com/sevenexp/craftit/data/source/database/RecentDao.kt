package com.sevenexp.craftit.data.source.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sevenexp.craftit.data.source.database.entity.RecentEntity

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent")
    fun getRecentItems(): PagingSource<Int, RecentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(items: List<RecentEntity>)

    @Query("DELETE FROM recent")
    suspend fun deleteAll()
}