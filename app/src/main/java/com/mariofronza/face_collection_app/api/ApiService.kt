package com.mariofronza.face_collection_app.api

import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.models.SessionRequest
import com.mariofronza.face_collection_app.models.SessionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    companion object {
        operator fun invoke(): ApiService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3333/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    @GET("photos")
    suspend fun getPhotos(@Header("Authorization") token: String): Response<List<Photo>>

    @POST("photos")
    suspend fun createPhoto(
        @Header("Authorization") token: String,
        @Body photo: Photo
    ): Response<Photo>

    @PUT("photos/{id}")
    suspend fun updatePhoto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body photo: Photo
    ): Response<Photo>


    @POST("sessions")
    suspend fun createSession(@Body request: SessionRequest): Response<SessionResponse>

}