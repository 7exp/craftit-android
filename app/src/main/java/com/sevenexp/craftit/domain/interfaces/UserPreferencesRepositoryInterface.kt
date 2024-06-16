package com.sevenexp.craftit.domain.interfaces

import com.sevenexp.craftit.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepositoryInterface {
    val getUserData: Flow<UserEntity>
    suspend fun saveUserData(userEntity: UserEntity)
    suspend fun clearUserData()
    suspend fun getUserId(): String
}