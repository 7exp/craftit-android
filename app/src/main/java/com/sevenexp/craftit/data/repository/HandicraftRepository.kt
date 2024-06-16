package com.sevenexp.craftit.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sevenexp.craftit.data.response.CreateLikeResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.GetHandicraftByIdResponse
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.data.source.database.HandicraftDatabase
import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.data.source.remote.paging.FypRemoteMediator
import com.sevenexp.craftit.domain.interfaces.HandicraftRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalPagingApi::class)
class HandicraftRepository(
    private val apiService: ApiService,
    private val database: HandicraftDatabase
) : HandicraftRepositoryInterface {
    override fun getFypStream(): Flow<PagingData<FypItems>> {
        val pagingSourceFactory = { database.fypDao().getFypItems() }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = FypRemoteMediator(apiService, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

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