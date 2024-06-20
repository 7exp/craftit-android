package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity

data class TrendingResponse(

    @field:SerializedName("pagination")
    val pagination: TrendingPagination,

    @field:SerializedName("data")
    val data: List<TrendingEntity>,

    @field:SerializedName("message")
    val message: String
)

data class TrendingPagination(

    @field:SerializedName("lastPage")
    val lastPage: Int,

    @field:SerializedName("pageSize")
    val pageSize: Int,

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("totalCount")
    val totalCount: Int
)