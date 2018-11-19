package com.test.photoeditor.internal.di

import com.test.photoeditor.data.repository.ImageDataRepository
import com.test.photoeditor.domain.interactor.ImageSave
import com.test.photoeditor.presentation.editor.EditorPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [(ImageRepositoryModule::class)])
class EditorModule {
    @Provides
    fun provideEditorPresenter(imageSave: ImageSave): EditorPresenter {
        return EditorPresenter(imageSave)
    }

    @Provides
    fun provideImageSave(imageDataRepository: ImageDataRepository): ImageSave {
        return ImageSave(imageDataRepository)
    }
}