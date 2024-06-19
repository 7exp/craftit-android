package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetDetailUseCase(
    private val handicraftRepository: HandicraftRepository,
) {
    operator fun invoke(id: String): Flow<ResultState<DetailResponse>> = flow {
        emit(ResultState.Loading())
        handicraftRepository.getHandicraftById(id).catch {
            emit(ResultState.Error(it.message.toString()))
        }.collect { response ->
            emit(ResultState.Success(response))
        }
    }
}