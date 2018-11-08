package com.test.photoeditor.presentation.gallery

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ImageItem
import kotlinx.android.synthetic.main.item_image.view.*

class ImagesAdapter(images: List<ImageItem>) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {
    private val imagesList = images

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImagesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.setItem(imagesList[position])
    }

    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.imageView

        fun setItem(image: ImageItem) {
            //val path = image.path
            //val bitmap = BitmapFactory.decodeFile(path)
            //imageView.setImageBitmap(bitmap)
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}