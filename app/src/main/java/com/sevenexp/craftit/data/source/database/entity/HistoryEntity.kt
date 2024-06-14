package com.sevenexp.craftit.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.Field

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey
    val id: String,
    val totalStep: Int,
    val image: String?,
    val name: String,
    val currentStep: Int = 0,
)
