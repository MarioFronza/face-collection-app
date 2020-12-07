package com.mariofronza.face_collection_app.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.utils.RequestErrorFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PhotosViewModel(
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private var _photos = MutableLiveData<List<Photo>>()
    private var _error = MutableLiveData<String>()

    val photos: LiveData<List<Photo>>
        get() = _photos

    val error: LiveData<String>
        get() = _error

    fun getAllPhotos(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.getPhotos(token)
            if (response.isSuccessful) {
                _photos.value = response.body()
            }
        }
    }

    fun createPhoto(token: String, photoType: String, photo: MultipartBody.Part) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.createPhoto(token, photoType, photo)
            if (response.isSuccessful) {
                getAllPhotos(token)
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

    fun updatePhoto(token: String, id: Int, photoType: String, photo: MultipartBody.Part) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = photosRepository.updatePhoto(token, id, photoType, photo)
            if (response.isSuccessful) {
                getAllPhotos(token)
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

}