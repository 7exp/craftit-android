package com.sevenexp.craftit.data.response

data class LoginResponse(
	val message: String? = null,
	val token: String? = null,
	val data: LoginDataResponse? = null
)

data class LoginDataResponse(
	val address: String? = null,
	val name: String? = null,
	val id: String? = null
)

