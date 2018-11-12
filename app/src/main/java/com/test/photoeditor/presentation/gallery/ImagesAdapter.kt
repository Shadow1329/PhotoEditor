package com.test.photoeditor.presentation.gallery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ImageItem
import kotlinx.android.synthetic.main.item_image.view.*
import android.graphics.BitmapFactory
import java.io.File


class ImagesAdapter(private val imagesList: List<ImageItem>) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

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
        private var currentImage = ""

        fun setItem(image: ImageItem) {
            imageView.setImageResource(R.drawable.ic_launcher_background)
            /*val fileName = image.thumbnail
            if (fileName != currentImage) {
                val imageFile = File(fileName)
                if (imageFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    imageView.setImageBitmap(myBitmap)
                    currentImage = fileName
                }
            }*/
        }
    }
}