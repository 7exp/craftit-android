package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File

class UpdatePictureUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(image: File): Flow<ResultState<UpdateProfilePictureResponse>> =
        flow {
            emit(ResultState.Loading())
            authRepository.updateProfilePicture(image).catch { err ->
                emit(ResultState.Error(err.message.toString()))
            }.collect {
                emit(ResultState.Success(it))
            }
        }
}