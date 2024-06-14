package com.sevenexp.craftit.data.response.items

import com.google.gson.annotations.SerializedName

data class HandicraftItem(

    @field:SerializedName("waste")
    val waste: List<String?>? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("user_photo")
    val userPhoto: String? = null,

    @field:SerializedName("totalStep")
    val totalStep: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id_user")
    val idUser: String? = null,

    @field:SerializedName("tags")
    val tags: List<String?>? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("totalImages")
    val totalImages: Int? = null,

    @field:SerializedName("createdBy")
    val createdBy: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("likes")
    val likes: Int? = null
)
