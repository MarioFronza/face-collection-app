package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.Photo
import okhttp3.MultipartBody

class PhotosRepository(
    private val api: ApiService
) {

    suspend fun getPhotos(token: String) = api.getPhotos("Bearer $token")

    suspend fun createPhoto(token: String, photoType: String, photo: MultipartBody.Part) =
        api.createPhoto("Bearer $token", photoType, photo)

    suspend fun updatePhoto(token: String, id: Int, photoType: String, photo: MultipartBody.Part) =
        api.updatePhoto("Bearer $token", id, photoType, photo)

}