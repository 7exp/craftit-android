package com.sevenexp.craftit.data.source.remote

import com.sevenexp.craftit.data.response.CreateUserResponse
import com.sevenexp.craftit.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): CreateUserResponse

    @POST("auth/login")
    suspend fun login(email: String, password: String): LoginResponse
}

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)