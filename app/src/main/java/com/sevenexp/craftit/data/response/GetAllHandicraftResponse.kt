package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName

data class GetAllHandicraftResponse(
	@field:SerializedName("data")
	val data: List<HandicraftItemResponse>
)