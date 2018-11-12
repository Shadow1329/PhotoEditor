package com.test.photoeditor.internal.di

import android.content.Context
import com.test.photoeditor.data.repository.ImageDataRepository
import dagger.Module
import dagger.Provides

@Module
class ImageRepositoryModule {
    @Provides
    fun provideImageDataRepository(context: Context): ImageDataRepository {
        return ImageDataRepository(context)
    }
}