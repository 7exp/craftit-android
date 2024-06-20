package com.sevenexp.craftit.data.response.items

import com.google.gson.annotations.SerializedName

data class HandicraftItems(
    @field:SerializedName("id") val id: String,

    @field:SerializedName("image") val image: String,

    @field:SerializedName("totalStep") val totalStep: Int,

    @field:SerializedName("createdAt") val createdAt: String,

    @field:SerializedName("name") val name: String,

    @field:SerializedName("id_user") val idUser: String,

    @field:SerializedName("createdBy") val createdBy: String,

    @field:SerializedName("image_user") val imageUser: String,

    @field:SerializedName("likes") val likes: Int,

    @field:SerializedName("tags") val tagsItems: List<String>
)
