package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.domain.entity.UserEntity
import com.sevenexp.craftit.domain.interfaces.UserPreferencesRepositoryInterface
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepositoryInterface
) {
    operator fun invoke(email: String, password: String): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading())
        authRepository.login(email, password).catch {
            emit(ResultState.Error(it.message.toString()))
        }.collect { res ->
            if (res.data != null && res.token != null) {
                res.data.let {
                    userPreferencesRepository.saveUserData(
                        UserEntity(
                            id = it.id,
                            name = it.name,
                            token = res.token
                        )
                    )
                    emit(ResultState.Success("Success"))
                }
            } else {
                emit(ResultState.Error(res.message.toString()))
            }
        }
    }
}