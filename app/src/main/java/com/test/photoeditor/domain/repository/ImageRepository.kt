package com.test.photoeditor.domain.repository

import com.test.photoeditor.domain.model.ImageItem
import io.reactivex.Completable
import io.reactivex.Single
import java.io.ByteArrayOutputStream

interface ImageRepository {
    fun getImages(): Single<List<ImageItem>>
    fun saveImage(fileName: String, byteStream: ByteArrayOutputStream): Completable
}