package com.sevenexp.craftit.domain.usecase

import androidx.paging.PagingData
import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.source.database.entity.RecentEntity
import kotlinx.coroutines.flow.Flow

class GetRecentUseCase(
    private val handicraftRepository: HandicraftRepository
) {
    operator fun invoke(): Flow<PagingData<RecentEntity>> = handicraftRepository.getRecentStream()
}