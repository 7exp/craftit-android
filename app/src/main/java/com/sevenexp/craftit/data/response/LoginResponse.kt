package com.sevenexp.craftit.data.response

data class LoginResponse(
	val message: String? = null,
	val token: String? = null,
	val data: LoginDataResponse? = null
)

data class LoginDataResponse(
	val address: String,
	val name: String,
	val id: String
)

