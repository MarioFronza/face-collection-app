package com.mariofronza.face_collection_app.listeners

import com.mariofronza.face_collection_app.models.Photo

interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(photo: Photo)

}