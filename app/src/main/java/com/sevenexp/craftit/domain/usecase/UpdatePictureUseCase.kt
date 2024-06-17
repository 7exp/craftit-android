package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.domain.interfaces.AuthRepositoryInterface
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class UpdatePictureUseCase(
    private val authRepository: AuthRepositoryInterface
) {
    operator fun invoke(image: File): Flow<ResultState<UpdateProfilePictureResponse>> =
        flow<ResultState<UpdateProfilePictureResponse>> {
            emit(ResultState.Loading())
            authRepository.updateProfilePicture(image).catch { err ->
                emit(ResultState.Error(err.message.toString()))
            }.collect {
                emit(ResultState.Success(it))
            }
        }.flowOn(Dispatchers.IO)
}