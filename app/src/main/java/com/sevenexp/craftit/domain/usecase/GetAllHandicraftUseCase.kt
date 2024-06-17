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
                            id = item.id,
                            image = item.image,
                            totalStep = item.totalStep,
                            createdAt = item.createdAt,
                            name = item.name,
                            idUser = item.idUser,
                            createdBy = item.createdBy,
                            imageUser = item.imageUser,
                            likes = item.likes,
                            tagsItems = item.tagsItems
                        )
                    }
                    emit(ResultState.Success(handicraftItems))
                }
            }
    }
}