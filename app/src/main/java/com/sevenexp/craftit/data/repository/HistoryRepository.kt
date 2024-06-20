package com.sevenexp.craftit.data.repository

import android.util.Log
import com.sevenexp.craftit.data.source.database.HandicraftDatabase
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.domain.interfaces.HistoryRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HistoryRepository(
    private val handicraftDatabase: HandicraftDatabase
) : HistoryRepositoryInterface {
    override fun getAllHistory(): Flow<List<HistoryEntity>> = flow {
        emit(handicraftDatabase.historyDao().getAllHistory())
    }.flowOn(Dispatchers.IO)

    override suspend fun insertHistory(historyEntity: HistoryEntity) {
        handicraftDatabase.historyDao().insertHistory(historyEntity)
    }

    override fun deleteHistory(historyEntity: HistoryEntity) {
        handicraftDatabase.historyDao().deleteHistory(historyEntity)
    }

    override fun deleteAllHistory() {
        handicraftDatabase.historyDao().deleteAll()
    }

    override suspend fun updateStep(historyEntity: HistoryEntity) {
        handicraftDatabase.historyDao().updateStep(historyEntity)
    }

}