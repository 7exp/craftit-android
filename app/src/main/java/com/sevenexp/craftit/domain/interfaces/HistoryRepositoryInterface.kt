package com.sevenexp.craftit.domain.interfaces

import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepositoryInterface {
    fun getAllHistory(): Flow<List<HistoryEntity>>
    suspend fun insertHistory(historyEntity: HistoryEntity)
    fun deleteHistory(historyEntity: HistoryEntity)
    fun deleteAllHistory()
    suspend fun updateStep(historyEntity: HistoryEntity)
}