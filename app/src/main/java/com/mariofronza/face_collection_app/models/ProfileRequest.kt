package com.mariofronza.face_collection_app.models

class ProfileRequest(
    val name: String,
    val email: String,
    val oldPassword: String,
    val password: String,
    val passwordConfirmation: String
)