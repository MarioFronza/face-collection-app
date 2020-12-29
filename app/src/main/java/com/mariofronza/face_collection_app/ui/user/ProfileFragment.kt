package com.mariofronza.face_collection_app.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.models.ProfileRequest
import com.mariofronza.face_collection_app.repositories.UsersRepository
import com.mariofronza.face_collection_app.utils.SessionManager
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var factory: UserViewModelFactory
    private lateinit var viewModel: UserViewModel
    private lateinit var saveProfileButton: CircularProgressButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val api = ApiService()
        val usersRepository = UsersRepository(api)
        sessionManager = SessionManager(requireActivity())
        factory = UserViewModelFactory(usersRepository)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        saveProfileButton = view.findViewById(R.id.saveProfileButton)

        viewModel.profileSuccess.observe(requireActivity(), Observer {
            if (it == true) {
                Toast.makeText(activity, "Sucesso ao alterar senha", Toast.LENGTH_SHORT).show()
                saveProfileButton.revertAnimation()
            }
        })

        viewModel.error.observe(requireActivity(), Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            saveProfileButton.revertAnimation()
        })

        saveProfileButton.setOnClickListener {
            prepareUpdate()
        }

        return view
    }

    private fun prepareUpdate() {
        val oldPassword = profileEditTextCurrentPass.text.toString()
        val password = profileEditTextNewPass.text.toString()
        val passwordConfirmation = profileEditTextConfirmPass.text.toString()

        if (oldPassword.isEmpty() || password.isBlank() || passwordConfirmation.isEmpty()) {
            Toast.makeText(activity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        } else {
            updateProfile(password, oldPassword, passwordConfirmation)
        }
    }

    private fun updateProfile(password: String, oldPassword: String, passwordConfirmation: String) {
        saveProfileButton.startAnimation()
        val profile = ProfileRequest(oldPassword, password, passwordConfirmation)
        val token = sessionManager.fetchAuthToken() ?: ""
        viewModel.updateProfile(token, profile)

    }


}