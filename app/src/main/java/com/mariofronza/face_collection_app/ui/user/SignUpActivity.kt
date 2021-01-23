package com.mariofronza.face_collection_app.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.SessionRequest
import com.mariofronza.face_collection_app.models.SessionResponse
import com.mariofronza.face_collection_app.models.StudentRequest
import com.mariofronza.face_collection_app.models.UserRequest
import com.mariofronza.face_collection_app.repositories.UsersRepository
import com.mariofronza.face_collection_app.ui.MainActivity
import com.mariofronza.face_collection_app.utils.SessionManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var factory: UserViewModelFactory
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val api = ApiService()
        val usersRepository = UsersRepository(api)
        sessionManager = SessionManager(this)
        factory = UserViewModelFactory(usersRepository)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        viewModel.user.observe(this, Observer {
            createStudent(it.id)
        })

        viewModel.student.observe(this, Observer {
            signUpSuccess()
        })

        viewModel.error.observe(this, Observer { message ->
            signUpFail(message)
        })

        tvLogin.setOnClickListener {
            goToSignInActivity()
        }

        btnSignUp.setOnClickListener {
            validateFields()
        }
    }


    private fun validateFields() {
        val name = etSignUpName.text.toString()
        val email = etSignUpEmail.text.toString()
        val enrollment = etSignUpEnrollment.text.toString()
        val password = etSignUpPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || enrollment.isEmpty() || password.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        } else {
            signUp(name, email, password)
        }
    }

    private fun createStudent(userId: Int) {
        val enrollment = etSignUpEnrollment.text.toString()
        val studentRequest = StudentRequest(enrollment, userId)
        viewModel.createStudent(studentRequest)
    }

    private fun signUp(name: String, email: String, password: String) {
        val userRequest = UserRequest(name, email, password)
        btnSignUp.startAnimation()
        viewModel.createUser(userRequest)
    }

    private fun signUpSuccess() {
        btnSignUp.revertAnimation()
        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show()
        goToSignInActivity()
    }

    private fun goToSignInActivity() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun signUpFail(message: String) {
        btnSignUp.revertAnimation()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}