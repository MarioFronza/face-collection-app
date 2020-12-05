package com.mariofronza.face_collection_app.ui.photos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.listeners.RecyclerViewClickListener
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.user.SignInActivity
import com.mariofronza.face_collection_app.utils.SessionManager

class PhotosFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var sessionManager: SessionManager
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var rvPhotos: RecyclerView
    private lateinit var photosViewModelFactory: PhotosViewModelFactory

    private val photos = mutableListOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)

        val api = ApiService()
        val photosRepository = PhotosRepository(api)

        rvPhotos = view.findViewById<View>(R.id.rvPhotos) as RecyclerView
        sessionManager = SessionManager(requireActivity())
        photosViewModelFactory = PhotosViewModelFactory(photosRepository)
        photosViewModel =
            ViewModelProvider(this, photosViewModelFactory).get(PhotosViewModel::class.java)

        val adapter = PhotosAdapter(photos, this)
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(requireActivity())

        photosViewModel.photos.observe(requireActivity(), Observer {
            photos.clear()
            photos.addAll(it)
            adapter.notifyDataSetChanged()
        })

        getAllFoods()

        return view
    }

    private fun getAllFoods() {
        val token = sessionManager.fetchAuthToken()
        if (token != null) {
            photosViewModel.getAllPhotos(token)
        }
    }

    override fun onRecyclerViewItemClick(photo: Photo) {
        Intent(activity, HandlePhotoActivity::class.java).also {
            startActivity(it)
        }
    }

}