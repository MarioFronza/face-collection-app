package com.mariofronza.face_collection_app.models

data class Photo(
    var id: Int,
    val path: String,
    var photoType: String,
    var updatedAt: String?,
    var url: String
)