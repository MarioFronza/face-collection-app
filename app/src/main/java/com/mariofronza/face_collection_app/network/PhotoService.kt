package com.mariofronza.face_collection_app.network

import com.mariofronza.face_collection_app.models.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PhotoService {

    @GET("photos")
    suspend fun getAllStudentPhotos(): Response<List<Photo>>

    @POST("photos")
    suspend fun createStudentPhoto(): Response<Photo>

}