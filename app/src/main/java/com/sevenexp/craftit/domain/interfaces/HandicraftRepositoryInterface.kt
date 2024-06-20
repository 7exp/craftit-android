package com.sevenexp.craftit.domain.interfaces

import androidx.paging.PagingData
import com.sevenexp.craftit.data.response.CreateLikeResponse
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.SearchResponse
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.data.source.database.entity.RecentEntity
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HandicraftRepositoryInterface {
    fun getHandicrafts(): Flow<GetAllHandicraftResponse>
    fun getHandicraftById(id: String): Flow<DetailResponse>
    fun likeHandicraft(id: Int): Flow<CreateLikeResponse>
    fun getFypStream(): Flow<PagingData<FypItems>>
    fun getTrendingStream(): Flow<PagingData<TrendingEntity>>
    fun getRecentStream(): Flow<PagingData<RecentEntity>>
    fun searchWithQuery(query: String): Flow<SearchResponse>
    fun searchWithImage(image: File): Flow<SearchResponse>

}