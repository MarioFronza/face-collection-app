package com.mariofronza.face_collection_app.models

class ProfileRequest(
    val oldPassword: String,
    val password: String,
    val passwordConfirmation: String
)