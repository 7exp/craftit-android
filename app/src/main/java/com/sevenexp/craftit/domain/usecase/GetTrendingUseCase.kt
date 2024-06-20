package com.sevenexp.craftit.domain.usecase

import androidx.paging.PagingData
import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity
import kotlinx.coroutines.flow.Flow

class GetTrendingUseCase(
    private val handicraftRepository: HandicraftRepository
) {
    operator fun invoke(): Flow<PagingData<TrendingEntity>> = handicraftRepository.getTrendingStream()
}