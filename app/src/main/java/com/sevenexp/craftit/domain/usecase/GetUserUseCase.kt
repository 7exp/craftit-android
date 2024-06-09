package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.response.LoginResponse
import com.sevenexp.craftit.domain.entity.UserEntity
import com.sevenexp.craftit.domain.interfaces.UserPreferencesRepositoryInterface
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(private val userPreferenceRepository: UserPreferencesRepositoryInterface) {
    operator fun invoke(): Flow<UserEntity> = userPreferenceRepository.getUserData
}