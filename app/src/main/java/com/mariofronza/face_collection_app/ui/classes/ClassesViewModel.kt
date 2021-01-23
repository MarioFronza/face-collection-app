package com.mariofronza.face_collection_app.ui.classes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mariofronza.face_collection_app.models.ClassResponse
import com.mariofronza.face_collection_app.repositories.ClassesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClassesViewModel(
    private val classesRepository: ClassesRepository
) : ViewModel() {

    private var _classes = MutableLiveData<ClassResponse>()
    private var _error = MutableLiveData<String>()

    val classes: LiveData<ClassResponse>
        get() = _classes

    val error: LiveData<String>
        get() = _error

    fun getAllClasses(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = classesRepository.getClasses(token)
            if (response.isSuccessful) {
                _classes.value = response.body() as ClassResponse
            }
        }
    }

}