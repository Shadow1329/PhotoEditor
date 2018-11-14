package com.test.photoeditor.presentation.gallery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ImageItem
import kotlinx.android.synthetic.main.item_image.view.*
import com.squareup.picasso.Picasso

class ImageAdapter(private val imagesList: List<ImageItem>, private val imageAdapterListener: ImageAdapterListener) : RecyclerView.Adapter<ImageAdapter.ImagesViewHolder>() {

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

    inner class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.imageView
        private var imageItem : ImageItem? = null

        init {
            imageView.setOnClickListener {
                imageItem?.let {
                    imageAdapterListener.onImageSelected(it)
                }
            }
        }

        fun setItem(image: ImageItem) {
            imageItem = image
            Picasso.get()
                    .load("file://${image.path}")
                    .resize(imageSize, imageSize)
                    .centerCrop()
                    .placeholder(R.color.colorImagePlaceHolder)
                    .into(imageView)
        }
    }

    companion object {
        const val imageSize = 150
    }
}