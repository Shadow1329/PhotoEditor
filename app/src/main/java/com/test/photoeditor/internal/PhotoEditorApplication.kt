package com.test.photoeditor.internal

import android.app.Application
import com.test.photoeditor.internal.di.*

class PhotoEditorApplication : Application() {
    private lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .galleryModule(GalleryModule())
                .editorModule(EditorModule())
                .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return mApplicationComponent
    }
}