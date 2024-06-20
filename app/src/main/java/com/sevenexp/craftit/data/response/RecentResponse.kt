package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.source.database.entity.RecentEntity

data class RecentResponse(
    @field:SerializedName("pagination")
    val pagination: RecentPagination,

    @field:SerializedName("data")
    val data: List<RecentEntity>,

    @field:SerializedName("message")
    val message: String
)

data class RecentPagination(

    @field:SerializedName("lastPage")
    val lastPage: Int,

    @field:SerializedName("pageSize")
    val pageSize: Int,

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("totalCount")
    val totalCount: Int
)