package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.Photo

class PhotosRepository(
    private val api: ApiService
) {

    suspend fun getPhotos(token: String) = api.getPhotos("Bearer $token")

    suspend fun createFood(token: String, photo: Photo) = api.createPhoto("Bearer $token", photo)

    suspend fun updateFood(token: String, id: Int, photo: Photo) =
        api.updatePhoto("Bearer $token", id, photo)

}