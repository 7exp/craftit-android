package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.HandicraftItemResponse
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetAllHandicraftUseCase(private val handicraftRepository: HandicraftRepository) {
    operator fun invoke(): Flow<ResultState<List<HandicraftItemResponse>>> = flow {
        emit(ResultState.Loading())
        handicraftRepository.getHandicrafts()
            .catch {
                emit(ResultState.Error(it.message ?: "An unexpected error occurred"))
            }
            .collect { it ->
                if (it.data.isEmpty()) {
                    emit(ResultState.Success(emptyList()))
                } else {
                    val handicraftItems = it.data.map { item ->
                        HandicraftItemResponse(
                            image = item.image,
                            createdAt = item.createdAt,
                            images = item.images,
                            like = item.like,
                            name = item.name,
                            step = item.step,
                            id = item.id,
                            idUser = item.idUser,
                            label = item.label,
                            user = item.user,
                            userPhoto = item.userPhoto,
                            updatedAt = item.updatedAt
                        )
                    }
                    emit(ResultState.Success(handicraftItems))
                }
            }
    }
}