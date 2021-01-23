package com.mariofronza.face_collection_app.api

import com.mariofronza.face_collection_app.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    companion object {
        operator fun invoke(): ApiService {
            val clientSetup = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()

            return Retrofit.Builder()
                .baseUrl("http:///")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientSetup)
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

    @GET("classes/teacher")
    suspend fun getClasses(
        @Header("Authorization") token: String,
    ): Response<ClassResponse>

    @PUT("profiles/student")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body profile: ProfileRequest
    ): Response<User>

    @Multipart
    @POST("recognize/{classId}")
    suspend fun recognize(
        @Header("Authorization") token: String,
        @Path("classId") classId: Int,
        @Part image: MultipartBody.Part
    ): Response<RecognizeResponse>

    @POST("sessions")
    suspend fun createSession(@Body request: SessionRequest): Response<SessionResponse>

    @POST("users")
    suspend fun createUser(@Body request: UserRequest): Response<User>

    @POST("students")
    suspend fun createStudent(@Body request: StudentRequest): Response<Student>

    @POST("refresh-tokens")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<SessionResponse>

}