package com.mariofronza.face_collection_app.network

import javax.inject.Inject

class RemotePhotoSource @Inject constructor(
    private val photoService: PhotoService
) : BaseNetworkSource() {

    suspend fun getAllStudentPhotos() = getResult { photoService.getAllStudentPhotos() }

    suspend fun createStudentPhoto() = getResult { photoService.createStudentPhoto() }

}