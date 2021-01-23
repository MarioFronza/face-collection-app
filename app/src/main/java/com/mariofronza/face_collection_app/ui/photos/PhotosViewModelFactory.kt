package com.mariofronza.face_collection_app.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.classes.ClassesViewModel
import com.mariofronza.face_collection_app.ui.classes.ClassesViewModelFactory
import com.mariofronza.face_collection_app.utils.SessionManager

@Suppress("UNCHECKED_CAST")
class PhotosViewModelFactory(
    private val photosRepository: PhotosRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotosViewModel(photosRepository) as T
    }

}