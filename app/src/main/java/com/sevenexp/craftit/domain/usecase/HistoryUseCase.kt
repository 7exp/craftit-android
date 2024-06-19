package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.HistoryRepository
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetAllHistoryUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke(): Flow<ResultState<List<HistoryEntity>>> =
        flow<ResultState<List<HistoryEntity>>> {
            emit(ResultState.Loading())
            historyRepository.getAllHistory().catch { e ->
                emit(ResultState.Error(e.message.toString()))
            }.collect { historyEntities ->
                emit(ResultState.Success(historyEntities))
            }
        }.flowOn(Dispatchers.IO)
}

class InsertHistoryUseCase(private val historyRepository: HistoryRepository) {
    suspend operator fun invoke(historyEntity: HistoryEntity) {
        historyRepository.insertHistory(historyEntity)
    }
}


class DeleteHistoryUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke(historyEntity: HistoryEntity) {
        historyRepository.deleteHistory(historyEntity)
    }
}

class DeleteAllHistoryUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke() {
        historyRepository.deleteAllHistory()
    }
}

class UpdateStepUseCase(private val historyRepository: HistoryRepository) {
    suspend operator fun invoke(historyEntity: HistoryEntity) {
        historyRepository.updateStep(historyEntity)
    }
}