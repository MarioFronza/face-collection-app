package com.mariofronza.face_collection_app.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.repositories.UsersRepository

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(usersRepository) as T
    }

}