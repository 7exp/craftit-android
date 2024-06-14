package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.items.HandicraftItems
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetAllHandicraftUseCase(private val handicraftRepository: HandicraftRepository) {
    operator fun invoke(): Flow<ResultState<List<HandicraftItems>>> = flow {
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
                        HandicraftItems(
                            image = item.image,
                            userPhoto = item.userPhoto,
                            totalStep = item.totalStep,
                            description = item.description,
                            idUser = item.idUser,
                            tags = item.tags,
                            createdAt = item.createdAt,
                            totalImages = item.totalImages,
                            createdBy = item.createdBy,
                            name = item.name,
                            id = item.id,
                            updatedAt = item.updatedAt,
                            likes = item.likes
                        )
                    }
                    emit(ResultState.Success(handicraftItems))
                }
            }
    }
}