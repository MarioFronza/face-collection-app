package com.mariofronza.face_collection_app.ui.photos

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.MainActivity
import kotlinx.android.synthetic.main.activity_confirm_photo.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class ConfirmPhotoActivity : AppCompatActivity() {

    private lateinit var token: String
    private var photoId: Int = 0
    private lateinit var photoType: String
    private lateinit var byteArray: ByteArray
    private lateinit var bitmap: Bitmap

    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var photosViewModelFactory: PhotosViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_photo)

        token = intent.getStringExtra("token").toString()
        photoId = intent.getIntExtra("photoId", 0)
        photoType = intent.getStringExtra("photoType").toString()
        byteArray = intent.getByteArrayExtra("bitmap")!!

        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        tvStudentPhoto.setImageBitmap(bitmap)

        val api = ApiService()
        val photosRepository = PhotosRepository(api)

        photosViewModelFactory = PhotosViewModelFactory(photosRepository)
        photosViewModel =
            ViewModelProvider(this, photosViewModelFactory).get(PhotosViewModel::class.java)

        photosViewModel.photos.observe(this, Observer {
            btnConfirmPhoto.revertAnimation()
            btnNewPhoto.visibility = View.VISIBLE
            goToMainActivity()
        })

        photosViewModel.error.observe(this, Observer {
            btnConfirmPhoto.revertAnimation()
            btnNewPhoto.visibility = View.VISIBLE
            Toast.makeText(this, "Erro ao realizar operação", Toast.LENGTH_SHORT).show()
        })
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }

    fun confirmPhoto(view: View) {
        btnConfirmPhoto.startAnimation()
        btnNewPhoto.visibility = View.GONE
        if (photoId == 0) {
            createPhoto()
        } else {
            updatePhoto()
        }
    }

    private fun createPhoto() {
        val photo = preparePhoto()
        photosViewModel.createPhoto(token, photoType, photo)
    }

    private fun updatePhoto() {
        val photo = preparePhoto()
        photosViewModel.updatePhoto(token, photoId, photoType, photo)
    }


    private fun convertFileToBitmap(file: File) {
        file.createNewFile()
        val os = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, os)
        os.close()
    }


    private fun preparePhoto(): MultipartBody.Part {
        val file = File(cacheDir, "student-photo");
        convertFileToBitmap(file);

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(
            "file",
            file.name + ".jpg", requestFile
        )
    }

    fun newPhoto(view: View) {
        finish()
    }
}