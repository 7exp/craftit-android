package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName

data class HandicraftItemResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("images")
	val images: Int? = null,

	@field:SerializedName("like")
	val like: Int = 0,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("step")
	val step: Int = 0,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("user_photo")
	val userPhoto: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
