package com.sevenexp.craftit.data.repository

import com.sevenexp.craftit.data.response.CreateLikeResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.GetHandicraftByIdResponse
import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.domain.interfaces.HandicraftRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HandicraftRepository(
    private val apiService: ApiService,
) : HandicraftRepositoryInterface {
    override fun getHandicrafts(): Flow<GetAllHandicraftResponse> = flow {
        emit(apiService.getAllHandicraft())
    }.flowOn(Dispatchers.IO)

    override fun getHandicraftById(id: Int): Flow<GetHandicraftByIdResponse> {
        TODO("Not yet implemented")
    }

    override fun likeHandicraft(id: Int): Flow<CreateLikeResponse> {
        TODO("Not yet implemented")
    }
}