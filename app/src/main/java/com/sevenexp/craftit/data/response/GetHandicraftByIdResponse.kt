package com.sevenexp.craftit.data.response

import com.google.gson.annotations.SerializedName

data class GetHandicraftByIdResponse(

	@field:SerializedName("data")
	val data: GetHandicraftByIdResponseData?,

	@field:SerializedName("message")
	val message: String
)

data class GetHandicraftByIdResponseData(

	@field:SerializedName("waste")
	val waste: List<String>? = List(0) { "" },

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createdBy")
	val createdBy: String,

	@field:SerializedName("totalStep")
	val totalStep: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("tags")
	val tags: List<String>? = List(0) { "" },

	@field:SerializedName("likes")
	val likes: Int
)
