package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.RefreshTokenRequest
import com.mariofronza.face_collection_app.models.SessionRequest

class UsersRepository(
    private val api: ApiService
) {

    suspend fun createSession(sessionRequest: SessionRequest) = api.createSession(sessionRequest)

    suspend fun refreshToken(refreshToken: RefreshTokenRequest) = api.refreshToken(refreshToken)

}