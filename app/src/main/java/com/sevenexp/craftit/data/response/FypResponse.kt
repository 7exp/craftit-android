package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.response.items.FypItems

data class FypResponse(

	@field:SerializedName("pagination")
	val pagination: TrendingPagination,

	@field:SerializedName("data")
	val data: List<FypItems>,

	@field:SerializedName("message")
	val message: String
)

data class FypPagination(

	@field:SerializedName("lastPage")
	val lastPage: Int,

	@field:SerializedName("pageSize")
	val pageSize: Int,

	@field:SerializedName("page")
	val page: String,

	@field:SerializedName("totalCount")
	val totalCount: Int
)
