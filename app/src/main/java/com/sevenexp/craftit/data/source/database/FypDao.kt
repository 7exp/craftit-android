package com.sevenexp.craftit.data.source.database

import com.sevenexp.craftit.data.response.items.FypItems
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FypDao {
    @Query("SELECT * FROM fyp")
    fun getFypItems(): PagingSource<Int, FypItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFyp(items: List<FypItems>)

    @Query("DELETE FROM fyp")
    suspend fun deleteAll()
}