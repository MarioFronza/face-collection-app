package com.mariofronza.face_collection_app.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mariofronza.face_collection_app.models.*
import com.mariofronza.face_collection_app.repositories.UsersRepository
import com.mariofronza.face_collection_app.utils.RequestErrorFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private var _session = MutableLiveData<SessionResponse>()
    private var _user = MutableLiveData<User>()
    private var _student = MutableLiveData<Student>()
    private var _error = MutableLiveData<String>()
    private var _profileSuccess = MutableLiveData<Boolean>()
    private var _userResponse = MutableLiveData<User>()

    val session: LiveData<SessionResponse>
        get() = _session

    val user: LiveData<User>
        get() = _user

    val student: LiveData<Student>
        get() = _student

    val userResponse: LiveData<User>
        get() = _userResponse

    val error: LiveData<String>
        get() = _error

    val profileSuccess: LiveData<Boolean>
        get() = _profileSuccess

    fun singIn(sessionRequest: SessionRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = usersRepository.createSession(sessionRequest)
            if (response.isSuccessful) {
                _session.value = response.body()
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

    fun createUser(userRequest: UserRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = usersRepository.createUser(userRequest)
            if (response.isSuccessful) {
                _user.value = response.body()
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

    fun createStudent(studentRequest: StudentRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = usersRepository.createStudent(studentRequest)
            if (response.isSuccessful) {
                _student.value = response.body()
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = usersRepository.refreshToken(refreshTokenRequest)
            if (response.isSuccessful) {
                _session.value = response.body()
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }

    fun updateProfile(token: String, profileRequest: ProfileRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = usersRepository.updateProfile(token, profileRequest)
            if (response.isSuccessful) {
                _profileSuccess.value = true
                _userResponse.value = response.body() as User
            } else {
                val errorMessage =
                    RequestErrorFormatter.formatErrorBody(response.errorBody()!!.string())
                _error.value = errorMessage
            }
        }
    }


}