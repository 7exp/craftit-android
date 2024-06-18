package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.data.response.GetUserDetailResponse
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetUserDetailUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<ResultState<GetUserDetailResponse>> = flow {
        emit(ResultState.Loading())
        authRepository.getUserDetail().catch {
            emit(ResultState.Error(it.message.toString()))
        }.collect { response ->
            emit(ResultState.Success(response))
        }
    }
}