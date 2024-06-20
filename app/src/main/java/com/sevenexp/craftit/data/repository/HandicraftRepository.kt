package com.sevenexp.craftit.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sevenexp.craftit.data.response.CreateLikeResponse
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.SearchResponse
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.data.source.database.HandicraftDatabase
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity
import com.sevenexp.craftit.data.source.local.UserPreferences
import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.data.source.remote.paging.FypRemoteMediator
import com.sevenexp.craftit.data.source.remote.paging.TrendingRemoteMediator
import com.sevenexp.craftit.domain.interfaces.HandicraftRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@OptIn(ExperimentalPagingApi::class)
class HandicraftRepository(
    private val apiService: ApiService,
    private val database: HandicraftDatabase,
    private val preferences: UserPreferences
) : HandicraftRepositoryInterface {
    override fun getFypStream(): Flow<PagingData<FypItems>> {
        val pagingSourceFactory = { database.fypDao().getFypItems() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = FypRemoteMediator(apiService, database, preferences),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getTrendingStream(): Flow<PagingData<TrendingEntity>> {
        val pagingSourceFactory = { database.trendingDao().getTrendingItems() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = TrendingRemoteMediator(apiService, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchWithQuery(query: String): Flow<SearchResponse> = flow {
        emit(apiService.search(query = query))
    }.flowOn(Dispatchers.IO)


    override fun searchWithImage(image: File): Flow<SearchResponse> = flow {
        val body = MultipartBody.Part.createFormData(
            "image", image.name, image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        emit(apiService.search(image = body))
    }.flowOn(Dispatchers.IO)

    override fun getHandicrafts(): Flow<GetAllHandicraftResponse> = flow {
        emit(apiService.getAllHandicraft())
    }.flowOn(Dispatchers.IO)

    override fun getHandicraftById(id: String): Flow<DetailResponse> = flow {
        emit(apiService.getHandicraftById(id))
    }.flowOn(Dispatchers.IO)

    override fun likeHandicraft(id: Int): Flow<CreateLikeResponse> {
        TODO("Not yet implemented")
    }

}