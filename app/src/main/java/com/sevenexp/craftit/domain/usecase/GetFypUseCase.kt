package com.sevenexp.craftit.domain.usecase

import androidx.paging.PagingData
import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.items.FypItems
import kotlinx.coroutines.flow.Flow

class GetFypUseCase(private val handicraftRepository: HandicraftRepository) {
    operator fun invoke(): Flow<PagingData<FypItems>> = handicraftRepository.getFypStream()

}