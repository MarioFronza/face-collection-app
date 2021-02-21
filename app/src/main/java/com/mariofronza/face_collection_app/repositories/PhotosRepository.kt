package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService
import okhttp3.MultipartBody

class PhotosRepository(
    private val api: ApiService
) {

    suspend fun getPhotos(token: String) = api.getPhotos("Bearer $token")

    suspend fun createPhoto(token: String, photoType: String, photo: MultipartBody.Part) =
        api.createPhoto("Bearer $token", photoType, photo)

    suspend fun updatePhoto(token: String, id: Int, photoType: String, photo: MultipartBody.Part) =
        api.updatePhoto("Bearer $token", id, photoType, photo)

    suspend fun recognize(token: String, classId: Int, photo: MultipartBody.Part) =
        api.recognize("Bearer $token", classId, photo)

    suspend fun update(token: String, studentId: Int, classId: Int, photo: MultipartBody.Part) =
        api.update("Bearer $token", studentId, classId, photo)

}