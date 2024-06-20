package com.sevenexp.craftit.data.source.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity

@Dao
interface TrendingDao {
    @Query("SELECT * FROM fyp")
    fun getTrendingItems(): PagingSource<Int, TrendingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrending(items: List<TrendingEntity>)

    @Query("DELETE FROM trending")
    suspend fun deleteAll()
}