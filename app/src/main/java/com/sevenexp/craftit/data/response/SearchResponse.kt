package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName
import com.sevenexp.craftit.data.response.items.HandicraftItems

data class SearchResponse(

    @field:SerializedName("data")
    val data: List<HandicraftItems>,

    @field:SerializedName("mesaage")
    val mesaage: String
)
