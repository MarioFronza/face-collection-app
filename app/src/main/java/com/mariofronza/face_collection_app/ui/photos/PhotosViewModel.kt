package com.mariofronza.face_collection_app.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private var _photos = MutableLiveData<List<Photo>>()


    val photos: LiveData<List<Photo>>
        get() = _photos

    fun getAllPhotos(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.getPhotos(token)
            if (response.isSuccessful) {
                _photos.value = response.body()
            }
        }
    }

    fun createPhoto(token: String, photo: Photo) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.createPhoto(token, photo)
            if (response.isSuccessful) {
                getAllPhotos(token)
            }
        }
    }

    fun updatePhoto(token: String, id: Int, photo: Photo) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.updatePhoto(token, id, photo)
            if (response.isSuccessful) {
                getAllPhotos(token)
            }
        }
    }

}