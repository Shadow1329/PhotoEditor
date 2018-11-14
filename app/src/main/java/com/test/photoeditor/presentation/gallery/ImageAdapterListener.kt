package com.test.photoeditor.presentation.gallery

import com.test.photoeditor.domain.model.ImageItem

interface ImageAdapterListener {
    fun onImageSelected(image: ImageItem)
}