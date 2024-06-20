package com.sevenexp.craftit.data.source.remote

import com.sevenexp.craftit.data.response.CreateUserResponse
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.data.response.FypResponse
import com.sevenexp.craftit.data.response.GetAllHandicraftResponse
import com.sevenexp.craftit.data.response.GetUserDetailResponse
import com.sevenexp.craftit.data.response.LoginResponse
import com.sevenexp.craftit.data.response.SearchResponse
import com.sevenexp.craftit.data.response.TrendingResponse
import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.data.source.remote.request.LoginRequest
import com.sevenexp.craftit.data.source.remote.request.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): CreateUserResponse

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @PUT("/image/user/{UserId}")
    @Multipart
    suspend fun updateProfilePicture(
        @Path("UserId") userId: String,
        @Part image: MultipartBody.Part
    ): UpdateProfilePictureResponse

    @GET("/auth/info")
    suspend fun getUserDetail(): GetUserDetailResponse

    @GET("/handicrafts")
    suspend fun getAllHandicraft(): GetAllHandicraftResponse

    @GET("/dashboard/fyp/{userid}")
    suspend fun getFyp(
        @Path("userid") userId: String,
        @Query("page") page: Int
    ): FypResponse

    @GET("/dashboard/trending")
    suspend fun getTrending(
        @Query("page") page: Int
    ): TrendingResponse

    @GET("/handicrafts/search")
    suspend fun search(
        @Query("query") query: String
    ): SearchResponse

    @POST("/recognition")
    @Multipart
    suspend fun search(
        @Part image: MultipartBody.Part
    ): SearchResponse

    @GET("/handicrafts/{HandicraftId}")
    suspend fun getHandicraftById(
        @Path("HandicraftId") handicraftId: String
    ): DetailResponse

}


