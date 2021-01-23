package com.mariofronza.face_collection_app.ui.classes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.listeners.RecyclerViewClickClassListener
import com.mariofronza.face_collection_app.models.Class
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.repositories.ClassesRepository
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.photos.HandlePhotoActivity
import com.mariofronza.face_collection_app.ui.photos.PhotosAdapter
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModel
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModelFactory
import com.mariofronza.face_collection_app.utils.SessionManager


class ClassesFragment : Fragment(), RecyclerViewClickClassListener {

    private lateinit var sessionManager: SessionManager
    private lateinit var rvClasses: RecyclerView
    private lateinit var classesViewModel: ClassesViewModel
    private lateinit var classesViewModelFactory: ClassesViewModelFactory

    private val classes = mutableListOf<Class>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_classes, container, false)

        val api = ApiService()
        val classesRepository = ClassesRepository(api)

        rvClasses = view.findViewById<View>(R.id.rvClasses) as RecyclerView
        sessionManager = SessionManager(requireActivity())
        classesViewModelFactory = ClassesViewModelFactory(classesRepository)
        classesViewModel =
            ViewModelProvider(this, classesViewModelFactory).get(ClassesViewModel::class.java)


        val adapter = ClassesAdapter(classes, this)
        rvClasses.adapter = adapter
        rvClasses.layoutManager = LinearLayoutManager(requireActivity())

        classesViewModel.classes.observe(requireActivity(), Observer {
            classes.clear()
            classes.addAll(it.data)
            adapter.notifyDataSetChanged()
        })

        getAllClasses()

        return view
    }

    private fun getAllClasses() {
        val token = sessionManager.fetchAuthToken()
        if (token != null) {
            classesViewModel.getAllClasses(token)
        }
    }

    override fun onRecyclerViewItemClick(classItem: Class) {
        Intent(activity, RecognizeActivity::class.java).also {
            it.putExtra("classId", classItem.id)
            startActivity(it)
        }
    }


}