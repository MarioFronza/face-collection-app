package com.mariofronza.face_collection_app.api

import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.models.RefreshTokenRequest
import com.mariofronza.face_collection_app.models.SessionRequest
import com.mariofronza.face_collection_app.models.SessionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    companion object {
        operator fun invoke(): ApiService {
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.102:3333/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    @GET("photos")
    suspend fun getPhotos(@Header("Authorization") token: String): Response<List<Photo>>

    @Multipart
    @POST("photos")
    suspend fun createPhoto(
        @Header("Authorization") token: String,
        @Query("photoType") photoType: String,
        @Part image: MultipartBody.Part
    ): Response<Photo>

    @Multipart
    @PUT("photos/{id}")
    suspend fun updatePhoto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("photoType") photoType: String,
        @Part image: MultipartBody.Part
    ): Response<Photo>


    @POST("sessions")
    suspend fun createSession(@Body request: SessionRequest): Response<SessionResponse>

    @POST("refresh-tokens")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<SessionResponse>

}