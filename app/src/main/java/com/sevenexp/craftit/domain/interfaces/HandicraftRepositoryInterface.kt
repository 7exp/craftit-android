package com.sevenexp.craftit.domain.interfaces

import androidx.paging.PagingData
import com.sevenexp.craftit.data.response.CreateLikeResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.GetHandicraftByIdResponse
import com.sevenexp.craftit.data.response.items.FypItems
import kotlinx.coroutines.flow.Flow

interface HandicraftRepositoryInterface {
    fun getHandicrafts(): Flow<GetAllHandicraftResponse>
    fun getHandicraftById(id: Int): Flow<GetHandicraftByIdResponse>
    fun likeHandicraft(id: Int): Flow<CreateLikeResponse>
    fun getFypStream(): Flow<PagingData<FypItems>>


}