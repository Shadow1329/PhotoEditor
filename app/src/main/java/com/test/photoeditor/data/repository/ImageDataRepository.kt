package com.test.photoeditor.data.repository

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.test.photoeditor.domain.model.ImageItem
import com.test.photoeditor.domain.repository.ImageRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.File


class ImageDataRepository(private val context: Context) : ImageRepository {
    override fun getImages(): Single<List<ImageItem>> {
        try {
            val imageList = mutableListOf<ImageItem>()
            val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED)

            val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            )

            if (cursor.moveToFirst()) {
                val imagePathCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                do {
                    imageList.add(ImageItem("", cursor.getString(imagePathCol)))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return Single.just(imageList)
        } catch (exception: Throwable) {
            return Single.error(exception)
        }
    }

    override fun saveImage(fileName: String, byteStream: ByteArrayOutputStream): Completable {
        return try {
            val path = Environment.getExternalStorageDirectory().toString() + "/PhotoEditor"

            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()

            val file = File(dir, "$fileName.png")

            val fileStream = FileOutputStream(file)
            byteStream.writeTo(fileStream)
            fileStream.close()

            MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, file.name, file.name)

            Completable.complete()
        } catch (exception: Throwable) {
            Completable.error(exception)
        }
    }
}