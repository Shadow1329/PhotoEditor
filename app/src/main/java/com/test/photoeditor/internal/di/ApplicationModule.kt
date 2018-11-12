package com.test.photoeditor.internal.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(application: Application) {

    private val mApplication = application

    @Provides
    @Singleton
    fun provideContext(): Context {
        return mApplication
    }
}