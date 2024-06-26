package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.data.source.local.UserPreferences
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File

class UpdatePictureUseCase(
    private val authRepository: AuthRepository,
    private val preferences: UserPreferences
) {
    operator fun invoke(image: File): Flow<ResultState<UpdateProfilePictureResponse>> =
        flow {
            emit(ResultState.Loading())
            val userId = preferences.getUserId()
            authRepository.updateProfilePicture(userId, image).catch { err ->
                emit(ResultState.Error(err.message.toString()))
            }.collect {
                emit(ResultState.Success(it))
            }
        }
}