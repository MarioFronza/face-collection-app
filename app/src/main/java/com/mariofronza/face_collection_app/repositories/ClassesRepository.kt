package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService

class ClassesRepository(
    private val api: ApiService
) {

    suspend fun getClasses(token: String) = api.getClasses("Bearer $token")

}