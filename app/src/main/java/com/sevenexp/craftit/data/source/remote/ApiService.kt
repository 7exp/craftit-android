package com.sevenexp.craftit.data.source.remote

import com.sevenexp.craftit.data.response.CreateUserResponse
import com.sevenexp.craftit.data.response.FypResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.LoginResponse
import com.sevenexp.craftit.data.source.remote.request.LoginRequest
import com.sevenexp.craftit.data.source.remote.request.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): CreateUserResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("handicrafts")
    suspend fun getAllHandicraft(): GetAllHandicraftResponse

    @GET("/dashboard/fyp")
    suspend fun getFyp(
        @Query("page") page: Int
    ): FypResponse
}


