package com.test.photoeditor.domain.repository

import com.test.photoeditor.domain.model.ImageItem
import io.reactivex.Single

interface ImageRepository {
    fun getImages(): Single<List<ImageItem>>
}