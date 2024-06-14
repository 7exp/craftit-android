package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.response.items.HandicraftItem

data class GetAllHandicraftResponse(
	@field:SerializedName("data")
	val data: List<HandicraftItem>
)