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
import com.mariofronza.face_collection_app.repositories.UsersRepository
import com.mariofronza.face_collection_app.ui.MainActivity
import com.mariofronza.face_collection_app.utils.SessionManager
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var factory: UserViewModelFactory
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val api = ApiService()
        val usersRepository = UsersRepository(api)
        sessionManager = SessionManager(this)
        factory = UserViewModelFactory(usersRepository)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        viewModel.session.observe(this, Observer { sessionResponse ->
            signInSuccess(sessionResponse)
        })

        viewModel.error.observe(this, Observer { message ->
            signInFail(message)
        })

        btnSignIn.setOnClickListener {
            validateFields()
        }

    }

    private fun validateFields() {
        val email = etSignInEmail.text.toString()
        val password = etSignInPassword.text.toString()

        if (email.isEmpty() || password.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        } else {
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        val session = SessionRequest(email, password)
        viewModel.singIn(session)
    }

    private fun signInSuccess(sessionResponse: SessionResponse) {
        sessionManager.saveAuthToken(sessionResponse.token)
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun signInFail(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}