package com.mariofronza.face_collection_app.repositories

import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.*

class UsersRepository(
    private val api: ApiService
) {

    suspend fun createSession(sessionRequest: SessionRequest) = api.createSession(sessionRequest)

    suspend fun createUser(userRequest: UserRequest) = api.createUser(userRequest)

    suspend fun createStudent(studentRequest: StudentRequest) = api.createStudent(studentRequest)

    suspend fun updateProfile(token: String, profileRequest: ProfileRequest) =
        api.updateProfile("Bearer $token", profileRequest)

    suspend fun refreshToken(refreshToken: RefreshTokenRequest) = api.refreshToken(refreshToken)

}