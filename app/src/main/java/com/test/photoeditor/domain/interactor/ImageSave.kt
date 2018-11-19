package com.test.photoeditor.domain.interactor

import com.test.photoeditor.domain.interactor.base.CompletableUseCase
import com.test.photoeditor.domain.repository.ImageRepository
import io.reactivex.Completable
import java.io.ByteArrayOutputStream

class ImageSave(private val imageRepository: ImageRepository): CompletableUseCase<Pair<String, ByteArrayOutputStream>>() {

    override fun buildUseCaseCompletable(param: Pair<String, ByteArrayOutputStream>): Completable {
        return imageRepository.saveImage(param.first, param.second)
    }
}