package com.mariofronza.face_collection_app.network

import com.mariofronza.face_collection_app.models.Photo
import retrofit2.Response
import retrofit2.http.POST

interface SessionService {

    @POST("sessions")
    suspend fun createSession(): Response<Photo>

}