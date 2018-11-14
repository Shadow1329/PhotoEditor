package com.test.photoeditor.internal.di

import com.test.photoeditor.presentation.editor.EditorPresenter
import dagger.Module
import dagger.Provides

@Module
class EditorModule {
    @Provides
    fun provideEditorPresenter(): EditorPresenter {
        return EditorPresenter()
    }
}