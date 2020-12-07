package com.mariofronza.face_collection_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.RefreshTokenRequest
import com.mariofronza.face_collection_app.models.SessionRequest
import com.mariofronza.face_collection_app.models.SessionResponse
import com.mariofronza.face_collection_app.repositories.UsersRepository
import com.mariofronza.face_collection_app.ui.user.SignInActivity
import com.mariofronza.face_collection_app.ui.user.UserViewModel
import com.mariofronza.face_collection_app.ui.user.UserViewModelFactory
import com.mariofronza.face_collection_app.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var factory: UserViewModelFactory
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

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

    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navHostFragment).navigateUp()
    }


    private fun goToSignInActivity() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun signInSuccess(sessionResponse: SessionResponse) {
        sessionManager.saveAuthToken(sessionResponse.token)
        sessionManager.saveRefreshToken(sessionResponse.refreshToken)
    }

    private fun signInFail(message: String) {
        goToSignInActivity()
    }

    private fun goToAppIntro() {
        Intent(this, AppIntroActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun refreshUserToken(refreshToken: String) {
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        viewModel.refreshToken(refreshTokenRequest)
    }

    override fun onStart() {
        super.onStart()
        val token = sessionManager.fetchAuthToken()
        val refreshToken = sessionManager.fetchRefreshToken()
        if (token == null) {
            goToAppIntro()
        } else {
            if (refreshToken != null) {
                refreshUserToken(refreshToken)
            } else {
                goToSignInActivity()
            }
        }

    }


}