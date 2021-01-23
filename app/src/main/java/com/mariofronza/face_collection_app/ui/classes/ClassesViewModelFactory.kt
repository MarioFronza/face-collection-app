package com.mariofronza.face_collection_app.ui.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.repositories.ClassesRepository

@Suppress("UNCHECKED_CAST")
class ClassesViewModelFactory(
    private val classesRepository: ClassesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClassesViewModel(classesRepository) as T
    }


}