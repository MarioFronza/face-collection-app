package com.mariofronza.face_collection_app.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.repositories.PhotosRepository

@Suppress("UNCHECKED_CAST")
class PhotosViewModelFactory(
    private val photosRepository: PhotosRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotosViewModel(photosRepository) as T
    }

}