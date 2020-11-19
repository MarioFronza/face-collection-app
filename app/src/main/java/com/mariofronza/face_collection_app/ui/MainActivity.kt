package com.mariofronza.face_collection_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.ui.photos.PhotosAdapter
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModel
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModelFactory
import com.mariofronza.face_collection_app.ui.user.SignInActivity
import com.mariofronza.face_collection_app.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var photosViewModelFactory: PhotosViewModelFactory

    private val photos = mutableListOf<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = ApiService()
        val photosRepository = PhotosRepository(api)

        sessionManager = SessionManager(this)
        photosViewModelFactory = PhotosViewModelFactory(photosRepository)
        photosViewModel =
            ViewModelProvider(this, photosViewModelFactory).get(PhotosViewModel::class.java)

        val adapter = PhotosAdapter(photos)
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(this)

        photosViewModel.photos.observe(this, Observer {
            photos.clear()
            photos.addAll(it)
            adapter.notifyDataSetChanged()
        })

        getAllFoods()
    }

    private fun getAllFoods() {
        val token = sessionManager.fetchAuthToken()
        if (token != null) {
            photosViewModel.getAllPhotos(token)
        }
    }

    private fun goToSignInActivity() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val token = sessionManager.fetchAuthToken()
        if (token == null) {
            goToSignInActivity()
        }
    }

}