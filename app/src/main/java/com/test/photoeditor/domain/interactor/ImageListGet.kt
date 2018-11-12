package com.test.photoeditor.domain.interactor

import com.test.photoeditor.domain.interactor.base.SingleUseCase
import com.test.photoeditor.domain.model.ImageItem
import com.test.photoeditor.domain.repository.ImageRepository
import io.reactivex.Single

class ImageListGet(private val imageRepository: ImageRepository): SingleUseCase<List<ImageItem>, Void?>() {

    override fun buildUseCaseSingle(param: Void?): Single<List<ImageItem>> {
        return imageRepository.getImages()
    }
}