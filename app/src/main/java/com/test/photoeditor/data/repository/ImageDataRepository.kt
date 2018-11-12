package com.test.photoeditor.data.repository

import android.content.Context
import android.provider.MediaStore
import com.test.photoeditor.domain.model.ImageItem
import com.test.photoeditor.domain.repository.ImageRepository
import io.reactivex.Single

class ImageDataRepository(private val context: Context) : ImageRepository {
    override fun getImages(): Single<List<ImageItem>> {
        try {
            val imageList = mutableListOf<ImageItem>()
            val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Thumbnails.DATA)

            val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            )

            if (cursor.moveToFirst()) {
                val imagePathCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val thumbnail = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
                do {
                    imageList.add(ImageItem("", cursor.getString(imagePathCol), cursor.getString(thumbnail)))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return Single.just(imageList)
        } catch (exception: Throwable) {
            return Single.error(exception)
        }
    }
}