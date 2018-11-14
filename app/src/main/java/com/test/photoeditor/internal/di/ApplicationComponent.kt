package com.test.photoeditor.internal.di

import com.test.photoeditor.presentation.editor.EditorActivity
import com.test.photoeditor.presentation.gallery.GalleryActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class), (GalleryModule::class), (EditorModule::class)])
interface ApplicationComponent {
    fun inject(activity: GalleryActivity)
    fun inject(activity: EditorActivity)
}