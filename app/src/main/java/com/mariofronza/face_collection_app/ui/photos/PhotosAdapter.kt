package com.mariofronza.face_collection_app.ui.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.models.Photo
import com.mariofronza.face_collection_app.models.PhotoType
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotosAdapter(
    private val photos: ArrayList<Photo>
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.itemView.apply {
            tvPhotoItemType.text = getFormattedPhotoType(photo.photoType.name)
            tvPhotoItemDate.text = photo.updatedAt
            Glide.with(context)
                .load(photo.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_profile)
                .into(ivPhotoItem)
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    private fun getFormattedPhotoType(photoTypeName: String): String {
        return when (photoTypeName) {
            PhotoType.NORMAL.name -> "Normal"
            PhotoType.SMILING.name -> "Sorrindo"
            PhotoType.CLOSED_EYES.name -> "Olhos fechados"
            PhotoType.RIGHT_SIDE.name -> "Lado direito"
            PhotoType.LEFT_SIDE.name -> "Lado esquerdo"
            else -> "Desconhecido"
        }
    }

}