package com.sevenexp.craftit.data.response

data class CreateUserResponse(
    val message: String,
    val data: UserDataResponse? = null
)

data class UserDataResponse(
    val name: String,
    val id: String,
    val email: String
)

