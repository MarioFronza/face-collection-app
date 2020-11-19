package com.mariofronza.face_collection_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.ui.photos.PhotosAdapter
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.models.PhotoType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photos = createFakePhotos()
        val adapter = PhotosAdapter(photos)

        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(this)

    }

    private fun createFakePhotos(): ArrayList<Photo> {
        val photos = arrayListOf<Photo>()
        photos.add(Photo(1, "", PhotoType.NORMAL, "", ""))
        photos.add(
            Photo(
                2,
                "",
                PhotoType.CLOSED_EYES,
                "",
                "https://avatars1.githubusercontent.com/u/26040800?s=400&u=0b1ff1492ac9de2d28124d9242c4d1adf1a28cda&v=4"
            )
        )
        photos.add(Photo(3, "", PhotoType.LEFT_SIDE, "", ""))
        photos.add(Photo(4, "", PhotoType.RIGHT_SIDE, "", ""))
        photos.add(Photo(5, "", PhotoType.SMILING, "", ""))

        return photos
    }

}