package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName

data class CreateLikeResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("id_handicraft")
	val idHandicraft: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
