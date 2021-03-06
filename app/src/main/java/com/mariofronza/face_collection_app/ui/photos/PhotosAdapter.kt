package com.mariofronza.face_collection_app.ui.photos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.listeners.RecyclerViewClickListener
import com.mariofronza.face_collection_app.models.Photo
import kotlinx.android.synthetic.main.photo_item.view.*
import java.text.SimpleDateFormat

class PhotosAdapter(
    private val photos: List<Photo>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]

        holder.itemView.apply {
            tvPhotoItemType.text = getFormattedPhotoType(photo.photoType)
            tvPhotoItemDate.text = photo.updatedAt?.let { getFormattedDate(it) }


            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(photo.url)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_profile)
                .into(ivPhotoItem)

            cvPhoto.setOnClickListener {
                listener.onRecyclerViewItemClick(photo)
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    private fun getFormattedPhotoType(photoTypeName: String): String {
        return when (photoTypeName) {
            "normal" -> "Normal"
            "smilling" -> "Sorrindo"
            "closedEyes" -> "Olhos fechados"
            "rightSide" -> "Lado direito"
            "leftSide" -> "Lado esquerdo"
            else -> "Desconhecido"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(parser.parse(date))
    }

}