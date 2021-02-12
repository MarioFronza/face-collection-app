package com.mariofronza.face_collection_app.ui.classes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.listeners.RecyclerViewClickClassListener
import com.mariofronza.face_collection_app.models.Class
import kotlinx.android.synthetic.main.class_item.view.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ClassesAdapter(
    private val classes: List<Class>,
    private val listener: RecyclerViewClickClassListener
) : RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classes[position]

        holder.itemView.apply {
            tvClassItemStartHour.text = getFormattedHour(classItem.startHour)
            tvClassItemEndHour.text = getFormattedHour(classItem.endHour)
            tvClassItemDate.text = ""
            tvClassItemSubjectName.text = classItem.subject.name

            cvClass.setOnClickListener {
                listener.onRecyclerViewItemClick(classItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedHour(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        parser.timeZone = TimeZone.getTimeZone("GMT")
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(parser.parse(date))
    }


    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(parser.parse(date))
    }

}