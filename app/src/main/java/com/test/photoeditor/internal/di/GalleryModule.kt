package com.test.photoeditor.internal.di

import android.content.Context
import com.test.photoeditor.data.repository.ImageDataRepository
import com.test.photoeditor.domain.interactor.ImageListGet
import com.test.photoeditor.presentation.gallery.GalleryPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [(ImageRepositoryModule::class)])
class GalleryModule {
    @Provides
    fun provideGalleryPresenter(context: Context, imageListGet: ImageListGet): GalleryPresenter {
        return GalleryPresenter(context, imageListGet)
    }

    @Provides
    fun provideImageListGet(imageDataRepository: ImageDataRepository): ImageListGet {
        return ImageListGet(imageDataRepository)
    }
}