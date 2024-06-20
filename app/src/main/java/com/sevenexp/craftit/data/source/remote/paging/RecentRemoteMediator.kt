package com.sevenexp.craftit.data.source.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sevenexp.craftit.data.source.database.HandicraftDatabase
import com.sevenexp.craftit.data.source.database.RemoteKeys
import com.sevenexp.craftit.data.source.database.entity.RecentEntity
import com.sevenexp.craftit.data.source.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class RecentRemoteMediator(
    private val apiService: ApiService,
    private val database: HandicraftDatabase,
) : RemoteMediator<Int, RecentEntity>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, RecentEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val response = apiService.getRecent(page)
            val endOfPaginationReached = response.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.recentDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.data.map { key ->
                    RemoteKeys(
                        id = key.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        source = "recent"
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                database.recentDao().insertRecent(response.data)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RecentEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id, "recent")
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RecentEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id, "recent")
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, RecentEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id, "recent")
            }
        }
    }
}
