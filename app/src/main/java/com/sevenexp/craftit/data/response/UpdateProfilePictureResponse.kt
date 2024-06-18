package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName

data class UpdateProfilePictureResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(
	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("token")
	val token: String,
)
