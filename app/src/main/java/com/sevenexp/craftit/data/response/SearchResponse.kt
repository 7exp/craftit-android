package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.response.items.FypItems

data class SearchResponse(

    @field:SerializedName("data")
    val data: List<FypItems>,

    @field:SerializedName("mesaage")
    val mesaage: String
)
