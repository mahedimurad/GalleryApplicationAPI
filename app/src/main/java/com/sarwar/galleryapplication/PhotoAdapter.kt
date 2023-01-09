package com.sarwar.galleryapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.sarwar.galleryapplication.model.ImageModel

class PhotoAdapter(val context: Context, val imageModels: ArrayList<ImageModel>, val onItemClick: (((ImageModel) -> Unit))) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(view: View) : ViewHolder(view){
        val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val imageModel = imageModels[position]

        //holder.ivImage.setImageResource(R.drawable.ic_image_placeholder)
        Glide.with(context)
            .load(imageModel.urls.thumb)
            .into(holder.ivImage)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(imageModels[position])
        }
    }

    override fun getItemCount(): Int {
        return imageModels.size
    }
}