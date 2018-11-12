package com.test.photoeditor.internal

import android.app.Application
import com.test.photoeditor.internal.di.ApplicationComponent
import com.test.photoeditor.internal.di.ApplicationModule
import com.test.photoeditor.internal.di.DaggerApplicationComponent
import com.test.photoeditor.internal.di.GalleryModule

class PhotoEditorApplication : Application() {
    private lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .galleryModule(GalleryModule())
                .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return mApplicationComponent
    }
}