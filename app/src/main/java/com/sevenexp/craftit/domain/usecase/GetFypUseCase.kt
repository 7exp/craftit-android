package com.sevenexp.craftit.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.items.FypItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFypUseCase(private val handicraftRepository: HandicraftRepository) {
    operator fun invoke(): Flow<PagingData<FypItems>> = flow {
        handicraftRepository.getFyp().map { pagingData ->
            pagingData.map { fypResponse ->
                FypItems(
                    image = fypResponse.image,
                    totalStep = fypResponse.totalStep,
                    name = fypResponse.name,
                    id = fypResponse.id,
                    idUser = fypResponse.idUser,
                    createdBy = fypResponse.createdBy,
                    imageUser = fypResponse.imageUser,
                    likes = fypResponse.likes,
                    tagsItems = fypResponse.tagsItems,
                )
            }
        }
    }
}