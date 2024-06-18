package com.sevenexp.craftit.data.response

data class GetUserDetailResponse(
    val data: GetUserDetailData,
    val message: String,
    val status: Boolean
)

data class GetUserDetailData(
    val image: String,
    val name: String,
    val id: String,
    val email: String
)

